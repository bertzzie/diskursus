package org.diskursus.verticle

import io.vertx.core.Vertx
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
@RunWith(VertxUnitRunner::class)
class MainVerticleTest {
    lateinit var vertx: Vertx

    @Before
    fun setUp(context: TestContext) {
        vertx = Vertx.vertx()
        vertx.deployVerticle(MainVerticle::class.java.name, context.asyncAssertSuccess())
    }

    @After
    fun tearDown(context: TestContext) {
        vertx.close(context.asyncAssertSuccess())
    }

    @Test
    fun testMainVerticle(context: TestContext) {
        val async = context.async()

        vertx.createHttpClient().getNow(8082, "localhost", "/", { response ->
            response.handler{ body ->
                context.assertTrue(body.toString().contains("Hello"))

                async.complete()
            }
        })
    }
}