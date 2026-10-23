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

package id.yoframework.core.extension.config

import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.config.configRetrieverOptionsOf
import io.vertx.kotlin.config.configStoreOptionsOf
import io.vertx.kotlin.coroutines.coAwait

private fun fileConfig(format: String, path: String): ConfigStoreOptions {
    return configStoreOptionsOf(
        type = "file",
        format = format,
        optional = true,
        config = JsonObject().put("path", path)
    )
}

fun propertiesConfig(path: String): ConfigStoreOptions = fileConfig("properties", path)

fun jsonConfig(path: String): ConfigStoreOptions = fileConfig("json", path)

fun yamlConfig(path: String): ConfigStoreOptions = fileConfig("yaml", path)

suspend fun Vertx.retrieveConfig(vararg stores: ConfigStoreOptions): JsonObject {
    val sysConfig = configStoreOptionsOf(type = "sys")
    val envConfig = configStoreOptionsOf(type = "env")

    val options = configRetrieverOptionsOf(
        stores = stores.asList() + sysConfig + envConfig
    )

    val retriever = ConfigRetriever.create(this, options)
    return retriever.config.coAwait()
}
