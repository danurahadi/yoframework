/*
 * Copyright (C) 2018 - Deny Prasetyo <jasoet87@gmail.com>
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

package id.yoframework.core.extension.json

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

data class Data(val number: Int, val text: String, val fraction: Double, val optional: Boolean)
data class RandomClass(val name: String)
class JsonTest {

    private val nullValue: Data? = null
    private val value = Data(10, "Some Text", 4.5, true)

    @Before
    fun before() {
        Json.mapper.apply {
            registerKotlinModule()
            registerModule(ParameterNamesModule())
            registerModule(JavaTimeModule())
        }
    }

    @Test
    fun `toJson should produce correct JsonObject based on receiver`() {
        val jsonObjectFromNull = nullValue.toJson()
        assertTrue(jsonObjectFromNull.isEmpty)

        val jsonObjectFromValue = value.toJson()
        assertFalse(jsonObjectFromValue.isEmpty)
    }

    @Test
    fun `mapTo should able to convert JsonObject to Type T`() {
        val jsonObjectFromValue = value.toJson()
        assertFalse(jsonObjectFromValue.isEmpty)

        val dataValue = jsonObjectFromValue.mapTo<Data>()
        assertTrue(value == dataValue)
    }

    @Test
    fun `toValue should able to convert JsonObject to Type T? and handle null receiver`() {
        val nullJsonObject: JsonObject? = null
        val valueFromNull = nullJsonObject.toValue<Data>()
        assertNull(valueFromNull)

        val jsonObjectFromValue = value.toJson()
        assertFalse(jsonObjectFromValue.isEmpty)

        val dataValue = jsonObjectFromValue.toValue<Data>()
        assertTrue(value == dataValue)


        val mustBeNull = jsonObjectFromValue.toValue<RandomClass>()
        assertNull(mustBeNull)

    }
}