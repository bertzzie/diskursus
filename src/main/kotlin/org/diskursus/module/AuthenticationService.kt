package org.diskursus.module

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class AuthenticationService: AuthProvider {
    override fun authenticate(authInfo: JsonObject, resultHandler: Handler<AsyncResult<User>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}