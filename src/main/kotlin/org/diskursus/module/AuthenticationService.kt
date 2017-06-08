package org.diskursus.module

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User
import org.diskursus.repository.UserRepository
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class AuthenticationService @Inject constructor(val userRepository: UserRepository): AuthProvider {
    override fun authenticate(authInfo: JsonObject, resultHandler: Handler<AsyncResult<User>>) {
        val username = authInfo.getString("username")
        val password = authInfo.getString("password")

        try {
            userRepository.authenticate(username, password).subscribe(
                    { result ->
                        if (result != null) {
                            // ini isi apa?
                            resultHandler.handle(Future.succeededFuture(result))
                        } else {
                            resultHandler.handle(Future.failedFuture(Exception("Wrong username or password")))
                        }
                    }
            )
        } catch (exception: Exception) {
            resultHandler.handle(Future.failedFuture("User not found in database"))
        }
    }
}