package org.diskursus.verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router
import org.diskursus.controller.MainController
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class MainVerticle @Inject constructor(val mainController: MainController): AbstractVerticle() {
    override fun start(startFuture: Future<Void>?) {
        val router = mainController.create()

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("HTTP_PORT", 8080), { result ->
                    if (result.succeeded()) {
                        startFuture?.complete()
                    } else {
                        startFuture?.fail(result.cause())
                    }
                })
    }
}