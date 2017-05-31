package org.diskursus.controller

import io.vertx.ext.web.Router
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class MainController @Inject constructor(override val router: Router): Controller({
    router.route("/").handler{ req ->
        req.response()
                .putHeader("content-type", "text/html")
                .end("<h1>Hello from my first Vert.x Application!</h1>")
    }
})