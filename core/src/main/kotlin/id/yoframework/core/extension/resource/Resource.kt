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

package id.yoframework.core.extension.resource

import id.yoframework.core.exception.DataInconsistentException
import id.yoframework.core.extension.logger.logger
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.ServerSocket
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

// String's own class loader only sees JDK resources, so fall back to the application class loader
private fun String.openResource(): InputStream? {
    return javaClass.getResourceAsStream(this)
        ?: Thread.currentThread().contextClassLoader?.getResourceAsStream(removePrefix("/"))
}

fun String.resourceToBuffer(): Buffer {
    val inputStream = openResource()
        ?: throw DataInconsistentException("Invalid string value.")
    return Buffer.buffer(inputStream.use { it.readAllBytes() })
}

fun String.pathToByteArray(): ByteArray {
    return FileInputStream(this).use { it.readAllBytes() }
}

fun String.readFileLine(): List<String> {
    return Files.readAllLines(Paths.get(this))
}

fun String.loadJsonObject(): JsonObject {
    val logger = logger(JsonObject::class)
    return try {
        val stream = openResource()
        val inputStream = stream?.let { InputStreamReader(it, StandardCharsets.UTF_8) }
        val jsonString = inputStream?.useLines { it.joinToString("") }

        logger.debug("Load config from $this")
        JsonObject(jsonString)
    } catch (e: IOException) {
        logger.debug("Config Cannot Loaded, Return Empty JsonObject.  Cause: ${e.message}")
        throw e
    }
}

fun tmpDir(): String {
    return System.getProperty("java.io.tmpdir")
}

fun homeDir(): String {
    return System.getProperty("user.home")
}

fun pathSeparator(): String {
    return File.pathSeparator
}

fun String.toBuffer(): Buffer {
    return Buffer.buffer(this)
}

fun randomPort(): Int {
    return ServerSocket(0).use { it.localPort }
}
