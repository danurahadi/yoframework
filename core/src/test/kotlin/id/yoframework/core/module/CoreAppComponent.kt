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

package id.yoframework.core.module

import dagger.Component
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.file.FileSystem
import io.vertx.core.json.JsonObject
import javax.inject.Singleton
import javax.validation.Validator

@Singleton
@Component(modules = [CoreModule::class])
interface CoreAppComponent {
    fun config(): JsonObject
    fun eventBus(): EventBus
    fun vertx(): Vertx
    fun fileSystem(): FileSystem
    fun validator(): Validator
}