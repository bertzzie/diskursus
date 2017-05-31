package org.diskursus.controller

import io.vertx.ext.web.Router

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
abstract class Controller(val handlers: Router.() -> Unit) {
    abstract val router: Router

    fun create(): Router {
        return router.apply { handlers() }
    }
}