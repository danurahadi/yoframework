/*
 * Copyright (C)2018 - Deny Prasetyo <jasoet87@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package id.yoframework.extra

import id.yoframework.extra.extension.command.Command
import id.yoframework.extra.extension.command.execute
import id.yoframework.extra.extension.regex.matchAll
import id.yoframework.extra.extension.regex.matchFirst
import id.yoframework.extra.extension.regex.regexPatterns
import id.yoframework.extra.snowflake.nextAlpha
import id.yoframework.extra.snowflake.nextId
import id.yoframework.extra.snowflake.parse
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ExtraHelperTest {

    @Test
    fun `parse must return machine id and increment encoded by nextId`() {
        val first = parse(nextId(7))
        val second = parse(nextAlpha(7))
        assertEquals(7, first.machineId)
        assertEquals(7, second.machineId)
        assertEquals(first.increment + 1, second.increment)
    }

    @Test
    fun `execute must pipe input and output streams`() {
        val output = ByteArrayOutputStream()
        val exitCode = Command.execute(listOf("cat"), input = ByteArrayInputStream("hello".toByteArray()), output = output)
        assertEquals(0, exitCode)
        assertEquals("hello", output.toString())
    }

    @Test
    fun `matchFirst and matchAll must return groups`() {
        val match = "a1 b2".matchFirst("([a-z])(\\d)")!!
        assertEquals(listOf("a", "1"), match.group)
        assertEquals(listOf("b2"), "a1 b2".matchAll("[a-z]\\d").drop(1).map { it.text })
        assertNull("abc".matchFirst("\\d"))
    }

    @Test
    fun `regexPatterns must join patterns and handle empty list`() {
        assertEquals("a - b", listOf("a".toRegex(), "b".toRegex()).regexPatterns())
        assertEquals("", emptyList<Regex>().regexPatterns())
    }
}
