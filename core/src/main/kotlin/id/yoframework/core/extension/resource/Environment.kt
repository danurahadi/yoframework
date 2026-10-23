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

import id.yoframework.core.extension.logger.logger
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
fun <T : Any> env(clazz: KClass<T>, key: String, defaultValue: T? = null): T {
    val value: String? = System.getenv(key)
    return if (value != null) {
        when (clazz) {
            String::class -> value as T
            Int::class -> value.toInt() as T
            Long::class -> value.toLong() as T
            Double::class -> value.toDouble() as T
            Float::class -> value.toFloat() as T
            Boolean::class -> value.toBoolean() as T
            else -> throw IllegalArgumentException("${clazz.qualifiedName} Not Supported")
        }
    } else {
        defaultValue ?: throw IllegalArgumentException("Illegal: $key not found and default value is null!")
    }
}

inline fun <reified T : Any> env(key: String, defaultValue: T? = null): T {
    return env(T::class, key, defaultValue)
}

inline fun <reified T : Any> applyEnv(key: String, defaultValue: T? = null, operation: (T) -> Unit) {
    val logger = logger("Resource Extension")
    try {
        val value = env(key, defaultValue)
        operation(value)
    } catch (e: IllegalArgumentException) {
        logger.info("Exception occurred ${e.message}, Operation ignored!")
    }
}
