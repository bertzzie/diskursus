package org.diskursus.controller

import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.diskursus.model.Comment
import org.diskursus.repository.CommentRepository
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class CommentController @Inject constructor(override val router: Router,
                                            override val vertx: Vertx,
                                            val commentRepository: CommentRepository): Controller({
    route("/count/:postId").handler{ req ->
        val postId = req.request().getParam("postId")

        commentRepository.getCommentCountByPost(postId).subscribe(
                { result ->
                    req.response()
                            .putHeader("content-type", "application/json")
                            .end(Json.encode(json {
                                obj(
                                        "postId" to postId,
                                        "commentCount" to result
                                )
                            }))
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
        val newComment = Comment.fromJson(req.bodyAsJson)

        commentRepository.createComment(newComment).subscribe(
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