package org.diskursus.controller

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.core.json.json
import org.diskursus.DiskursusConfiguration
import org.diskursus.model.*
import javax.inject.Inject
import org.diskursus.repository.UserRepository
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class UserController @Inject constructor(override val router: Router,
                                         override val vertx: Vertx,
                                         val userRepository: UserRepository,
                                         val authProvider: AuthProvider): Controller({

    router.route(HttpMethod.POST, "/authenticate").handler(BodyHandler.create())
    router.route(HttpMethod.POST, "/authenticate").handler{ req ->
        val userData = req.bodyAsJson
        authProvider.authenticate(userData, { res ->
            if(res.succeeded()) {
                val result = res.result()
                val user = User.fromToJson(result.principal())
                val response = LoginResponse(true, user)

                req.session().put(DiskursusConfiguration.UserLoginSessionKey, true)
                req.session().put(DiskursusConfiguration.UserInfoSessionKey, user)

                req.response()
                   .putHeader("Content-Type", "application/json")
                   .end(Json.encode(response.toJson()))
            } else {
                val response = ErrorResponse("User atau password salah.", "400")

                req.response()
                   .setStatusCode(400)
                   .putHeader("Content-Type", "application/json")
                   .end(Json.encode(response.toJson()))
            }
        })
    }

    router.route("/logout").handler(MustAuthenticateHandler)
    router.route("/logout").handler { req ->
        req.session().destroy()

        req.response()
           .setStatusCode(302)
           .putHeader("Location", DiskursusConfiguration.AppHostname)
           .end()
    }

    router.route(HttpMethod.POST, "/register").handler(BodyHandler.create().setMergeFormAttributes(true))
    router.route(HttpMethod.POST, "/register").handler{ req ->
        val formData = req.request().formAttributes()
        val data = Register(
                username = formData.get("username"),
                password = formData.get("password"),
                retypePassword = formData.get("retypePassword")
        )

        if(Register.validate(data)) {
            val user = User(
                    data.username,
                    "",
                    BCrypt.hashpw(data.password, BCrypt.gensalt()),
                    DiskursusConfiguration.DefaultUserPic,
                    UserStatus.ACTIVE,
                    UserRole.STANDARD,
                    null,
                    DateTime.now()
            )

            userRepository.addUser(user).subscribe(
                    { _ ->
                        req.response()
                           .setStatusCode(201)
                           .putHeader("Content-Type", "application/json")
                           .end(Json.encode(user.toPublicJson()))
                    },
                    { err ->
                        val error = ErrorResponse("Pengguna sudah ada.", "500")
                        req.response()
                           .setStatusCode(500)
                           .putHeader("Content-Type", "application/json")
                           .end(Json.encode(error.toJson()))
                    }
            )
        } else {
            val error = ErrorResponse("Password dan ulangi tidak sama.", "400")
            req.response()
               .setStatusCode(400)
               .putHeader("Content-Type", "application/json")
               .end(Json.encode(error.toJson()))
        }
    }

    router.route("/info").handler(MustAuthenticateHandler)
    router.route("/info").handler{ req ->
        val user = req.session().get<User>(DiskursusConfiguration.UserInfoSessionKey)
        val isLoggedIn = req.session().get<Boolean>(DiskursusConfiguration.UserLoginSessionKey)
        val userInfo = UserInfo(
                isLoggedIn ?: false,
                user?.name,
                user?.email,
                user?.picture,
                user?.role?.toString()
        )

        req.response()
           .putHeader("Content-Type", "application/json")
           .end(Json.encode(userInfo.toJson()))
    }

    router.route("/list").handler(MustAuthenticateHandler)
    router.route("/list").handler(MustBeAdminHandler)
    router.route("/list").handler{ req ->
        val users = userRepository.getAllUsers()
        users.subscribe(
                { res ->
                    val sb = StringBuffer()
                    for (user in res) {
                        sb.append(Json.encode(user.toPublicJson()))
                    }

                    req.response()
                       .putHeader("Content-Type", "application/json")
                       .end(sb.toString())
                },
                { err ->
                    val error = ErrorResponse(err.message.orEmpty(), "500")
                    req.response()
                       .setStatusCode(500)
                       .putHeader("Content-Type", "application/json")
                       .end(Json.encode(error.toJson()))
                }
        )
    }

    router.route("/:name").handler{ req ->
        val name = req.request().getParam("name")
        val user = userRepository.getUserData(name)
        user.subscribe(
                { res ->
                    if (res == null) {
                        val error = ErrorResponse("User tidak ditemkan", "404")
                        req.response()
                           .setStatusCode(404)
                           .putHeader("Content-Type", "application/json")
                           .end(Json.encode(error.toJson()))
                    } else {
                        req.response()
                           .putHeader("Content-Type", "application/json")
                           .end(Json.encode(res.toPublicJson()))
                    }
                },
                { err ->
                    val error = ErrorResponse(err.message.orEmpty(), "500")
                    req.response()
                       .setStatusCode(500)
                       .putHeader("Content-Type", "application/json")
                       .end(Json.encode(error.toJson()))
                }
        )
    }

    router.route(HttpMethod.DELETE, "/:name/delete").handler(MustAuthenticateHandler)
    router.route(HttpMethod.DELETE, "/:name/delete").handler(MustBeAdminHandler)
    router.route(HttpMethod.DELETE, "/:name/delete").handler{ req ->
        val name = req.request().getParam("name")
        val user = userRepository.removeUser(name)
        user.subscribe(
                { _ ->
                    req.response()
                       .setStatusCode(204)
                       .putHeader("Content-Type", "application/json")
                       .end()
                },
                { err ->
                    val error = ErrorResponse(err.message.orEmpty(), "500")
                    req.response()
                       .setStatusCode(500)
                       .putHeader("Content-Type", "application/json")
                       .end(Json.encode(error.toJson()))
                }
        )
    }

    router.route(HttpMethod.PUT, "/add").handler(MustAuthenticateHandler)
    router.route(HttpMethod.PUT, "/add").handler(MustBeAdminHandler)
    router.route(HttpMethod.PUT, "/add").handler(BodyHandler.create())
    router.route(HttpMethod.PUT, "/add").handler{ req ->
        val newUser = User.fromJson(req.bodyAsJson)

        userRepository.addUser(newUser).subscribe(
                { _ ->
                    req.response()
                       .setStatusCode(201)
                       .putHeader("Content-Type", "application/json")
                       .end(req.bodyAsString)
                },
                { err ->
                    val error = ErrorResponse(err.message.orEmpty(), "500")
                    req.response()
                       .setStatusCode(500)
                       .putHeader("Content-Type", "application/json")
                       .end(Json.encode(error.toJson()))
                }
        )
    }

    router.route(HttpMethod.POST, "/update").handler(MustAuthenticateHandler)
    router.route(HttpMethod.POST, "/update").handler(MustBeAdminHandler)
    router.route(HttpMethod.POST, "/update").handler(BodyHandler.create())
    router.route(HttpMethod.POST, "/update").handler{ req ->
        val newUser = User.fromJson(req.bodyAsJson)

        userRepository.updateUser(newUser).subscribe(
                { _ ->
                    req.response()
                       .putHeader("Content-Type", "application/json")
                       .end(req.bodyAsString)
                },
                { err ->
                    val error = ErrorResponse(err.message.orEmpty(), "500")
                    req.response()
                       .setStatusCode(500)
                       .putHeader("Content-Type", "application/json")
                       .end(Json.encode(error.toJson()))
                }
        )
    }
})