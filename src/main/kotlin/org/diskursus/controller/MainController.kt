package org.diskursus.controller

import io.vertx.core.json.Json
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
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

    router.route("/userss").handler{ req ->
        client.find("users", json { obj() }, { res ->
            val sb = StringBuffer()
            for (result in res.result()) {
                sb.append(Json.encode(result))
            }

            req.response()
               .putHeader("content-type", "application/json")
               .end(sb.toString())
        })
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
})