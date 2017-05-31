package org.diskursus.controller

import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.diskursus.model.User
import org.diskursus.repository.impl.UserRepositoryImpl
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class MainController @Inject constructor(override val router: Router,
                                         val userRepositoryImpl: UserRepositoryImpl,
                                         val client: MongoClient): Controller({
    router.route("/").handler{ req ->
        req.response()
                .putHeader("content-type", "text/html")
                .end("<h1>Hello from my first Vert.x Application!</h1>")
    }

    router.route("/users").handler{ req ->
        val users = userRepositoryImpl.getAllUsers()
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

    router.route("/user/:name").handler{ req ->
        val name = req.request().getParam("name")
        val user = userRepositoryImpl.getUserData(name)
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

    router.route(HttpMethod.PUT, "/user").handler(BodyHandler.create())
    router.route(HttpMethod.PUT, "/user").handler{ req ->
        val newUser = User.fromJson(req.bodyAsJson)

        userRepositoryImpl.addUser(newUser).subscribe(
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