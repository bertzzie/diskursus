package org.diskursus.controller

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.CookieHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import org.diskursus.DiskursusConfiguration

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
abstract class Controller(val handlers: Router.() -> Unit) {
    abstract val router: Router
    abstract val vertx: Vertx

    fun create(): Router {
        return router.apply {
            router.route().handler(CookieHandler.create())
            router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

            handlers()
        }
    }

    companion object {
        val MustAuthenticateHandler = { context: RoutingContext ->
            val session = context.session()
            val isLoggedIn: Boolean? = session.get(DiskursusConfiguration.UserLoginSessionKey)

            if (isLoggedIn != null && isLoggedIn) {
                context.next()
            } else {
                context.response()
                        .setStatusCode(403)
                        .end()
            }
        }
    }
}