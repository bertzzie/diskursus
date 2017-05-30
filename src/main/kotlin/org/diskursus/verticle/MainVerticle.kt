package org.diskursus.verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class MainVerticle: AbstractVerticle() {
    override fun start(startFuture: Future<Void>?) {
        val router = Router.router(vertx)

        router.route("/").handler{ req ->
            req.response()
               .putHeader("content-type", "text/html")
               .end("<h1>Hello from my first Vert.x Application!</h1>")
        }

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