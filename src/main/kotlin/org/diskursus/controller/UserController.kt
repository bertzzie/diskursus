package org.diskursus.controller

import io.vertx.ext.web.Router
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.web.handler.BodyHandler
import javax.inject.Inject
import org.diskursus.model.User
import org.diskursus.repository.UserRepository

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class UserController @Inject constructor(override val router: Router,
                                         val userRepository: UserRepository,
                                         val authProvider: AuthProvider): Controller({

    router.route(HttpMethod.POST, "/authenticate").handler(BodyHandler.create())
    router.route(HttpMethod.POST, "/authenticate").handler{ req ->
        val userData = req.bodyAsJson
        authProvider.authenticate(userData, { res ->
            if(res.succeeded()) {
                val result = res.result()

                req.response().putHeader("content-type", "text/plain").end("Auth success!")
            } else {
                req.response().putHeader("content-type", "text/plain").end("Auth failed!!")
            }
        })
    }

    router.route("/list").handler{ req ->
        val users = userRepository.getAllUsers()
        users.subscribe(
                { res ->
                    val sb = StringBuffer()
                    for (user in res) {
                        sb.append(Json.encode(user.toJson()))
                    }

                    req.response()
                            .putHeader("content-type", "application/json")
                            .end(sb.toString())
                },
                { err ->
                    req.response()
                            .putHeader("content-type", "text/html")
                            .end(err.toString())
                }
        )
    }

    router.route("/:name").handler{ req ->
        val name = req.request().getParam("name")
        val user = userRepository.getUserData(name)
        user.subscribe(
                { res ->
                    req.response()
                            .putHeader("content-type", "application/json")
                            .end(Json.encode(res.toJson()))
                },
                { err ->
                    req.response()
                            .putHeader("content-type", "text/html")
                            .end(err.toString())
                }
        )
    }

    router.route(HttpMethod.DELETE, "/:name/delete").handler{ req ->
        val name = req.request().getParam("name")
        val user = userRepository.removeUser(name)
        user.subscribe(
                { res ->
                    req.response()
                            .putHeader("content-type", "text/plain")
                            .end("User $name deleted!")
                },
                { err ->
                    req.response()
                            .putHeader("content-type", "text/html")
                            .end(err.toString())
                }
        )
    }

    router.route(HttpMethod.PUT, "/add").handler(BodyHandler.create())
    router.route(HttpMethod.PUT, "/add").handler{ req ->
        val newUser = User.fromJson(req.bodyAsJson)

        userRepository.addUser(newUser).subscribe(
                { res ->
                    req.response()
                            .putHeader("content-type", "application/json")
                            .end(req.bodyAsString)
                },
                { err ->
                    req.response()
                            .putHeader("content-type", "text/html")
                            .end(err.toString())
                }
        )
    }

    router.route(HttpMethod.POST, "/update").handler(BodyHandler.create())
    router.route(HttpMethod.POST, "/update").handler{ req ->
        val newUser = User.fromJson(req.bodyAsJson)

        userRepository.updateUser(newUser).subscribe(
                { res ->
                    req.response()
                            .putHeader("content-type", "application/json")
                            .end(req.bodyAsString)
                },
                { err ->
                    req.response()
                            .putHeader("content-type", "text/html")
                            .end(err.toString())
                }
        )
    }
})