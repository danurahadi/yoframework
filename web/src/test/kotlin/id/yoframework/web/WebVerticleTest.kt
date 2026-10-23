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

package id.yoframework.web

import id.yoframework.core.extension.resource.randomPort
import id.yoframework.core.extension.vertx.buildVertx
import id.yoframework.web.controller.Controller
import id.yoframework.web.extension.jsonHandler
import id.yoframework.web.verticle.WebVerticle
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.core.deploymentOptionsOf
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WebVerticleTest {
    private val vertx: Vertx = buildVertx()

    private val controller = object : Controller({ get("/ping").jsonHandler { mapOf("pong" to true) } }) {
        override val router: Router = Router.router(vertx)
    }

    @AfterEach
    fun tearDown() {
        vertx.close()
    }

    @Test
    fun `deployment must fail when http server cannot start`() {
        val result = runCatching {
            runBlocking { vertx.deployVerticle(WebVerticle(vertx, controller), deploymentOptionsOf()).coAwait() }
        }
        assertTrue(result.isFailure)
    }

    @Test
    fun `wireJson must send content-length instead of chunked body`(): Unit = runBlocking {
        val port = randomPort()
        val config = JsonObject()
            .put("HTTP_PORT", port)
            .put("HTTP_COMPRESSION_SUPPORTED", false)
            .put("HTTP_COMPRESSION_LEVEL", 6)
        vertx.deployVerticle(WebVerticle(vertx, controller), deploymentOptionsOf(config = config)).coAwait()

        val response = WebClient.create(vertx).get(port, "localhost", "/ping").send().coAwait()
        assertEquals(JsonObject().put("pong", true), response.bodyAsJsonObject())
        assertNull(response.getHeader("Transfer-Encoding"))
        assertEquals(response.body()!!.length().toString(), response.getHeader("Content-Length"))
    }
}
