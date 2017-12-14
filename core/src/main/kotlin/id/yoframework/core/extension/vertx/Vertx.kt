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

package id.yoframework.core.extension.vertx

import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.awaitResult

suspend fun Vertx.createHttpServer(port: Int, handler: (HttpServerRequest) -> Unit): HttpServer {
    val httpServer = this.createHttpServer()
            .requestHandler(handler)
    return awaitResult { httpServer.listen(port, it) }
}

suspend fun Vertx.deployVerticle(verticle: Verticle, config: JsonObject, worker: Boolean = false): String {
    val option = DeploymentOptions().apply {
        this.config = config
        this.isWorker = worker
    }

    return awaitResult { this.deployVerticle(verticle, option, it) }
}

suspend fun buildClustereVertx(option: VertxOptions): Vertx {
    return awaitResult { Vertx.clusteredVertx(option, it) }
}

fun buildVertx(option: VertxOptions): Vertx {
    return Vertx.vertx(option)
}

fun buildVertx(): Vertx {
    return buildVertx(VertxOptions())
}