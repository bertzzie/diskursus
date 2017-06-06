package org.diskursus.controller

import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
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