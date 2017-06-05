package org.diskursus.controller

import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.diskursus.ext.single
import org.diskursus.model.FullPost
import org.diskursus.model.Post
import org.diskursus.repository.PostRepository
import org.diskursus.repository.UserRepository
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class PostController @Inject constructor(override val router: Router,
                                         override val vertx: Vertx,
                                         val postRepository: PostRepository,
                                         val userRepository: UserRepository): Controller({

    route("/list").handler{ req ->
        val cursor = req.request().getParam("cursor")
        val perPage = req.request().getParam("per_page") ?: "5"

        postRepository.getPosts(cursor, perPage.toInt()).subscribe(
                { result ->
                    req.response()
                       .putHeader("content-type", "application/json")
                       .end(Json.encode(result))
                },
                { err ->
                    req.response()
                       .putHeader("content-type", "text/html")
                       .end(err.toString())
                }
        )
    }

    route(HttpMethod.PUT, "/add").handler(MustAuthenticateHandler)
    route(HttpMethod.PUT, "/add").handler(BodyHandler.create())
    route(HttpMethod.PUT, "/add").handler{ req ->
        val newPost = Post.fromJson(req.bodyAsJson)
        postRepository.createPost(newPost).subscribe(
                { result ->
                    req.response()
                       .setStatusCode(201)
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