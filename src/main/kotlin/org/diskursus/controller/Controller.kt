package org.diskursus.controller

import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.CookieHandler
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import org.diskursus.DiskursusConfiguration
import org.diskursus.model.User
import org.diskursus.model.UserRole

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
            val sessionHandler = SessionHandler.create(LocalSessionStore.create(vertx))
                                               .setCookieHttpOnlyFlag(true)
                                               .setCookieSecureFlag(true)

            val corsHandler = CorsHandler.create(DiskursusConfiguration.AppHostname)
                    .allowedMethods(setOf(HttpMethod.GET, HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE))
                    .allowedHeader("Content-Type")
                    .allowCredentials(true)

            router.route().handler(CookieHandler.create())
            router.route().handler(sessionHandler)
            router.route().handler(corsHandler)
            router.route().handler { context ->
                context.response()
                       .putHeader("X-Content-Type-Options", "nosniff")
                       .putHeader("Strict-Transport-Security", "max-age=${15768000}")
                       .putHeader("X-Download-Options", "noopen")
                       .putHeader("X-XSS-Protection", "1; mode=block")
                       .putHeader("X-FRAME-OPTIONS", "DENY")

                context.next()
            }

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

        val MustBeAdminHandler = { context: RoutingContext ->
            val session = context.session()
            val user = session.get<User>(DiskursusConfiguration.UserInfoSessionKey)
            when(user.role) {
                UserRole.ADMIN -> context.next()
                else -> context.response()
                               .setStatusCode(403)
                               .end()
            }
        }
    }
}