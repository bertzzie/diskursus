package org.diskursus.controller

import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.diskursus.DiskursusConfiguration
import org.diskursus.ext.single
import org.diskursus.model.FullPost
import org.diskursus.model.Post
import org.diskursus.model.User
import org.diskursus.repository.CommentRepository
import org.diskursus.repository.PostRepository
import org.diskursus.repository.UserRepository
import java.util.*
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class PostController @Inject constructor(override val router: Router,
                                         override val vertx: Vertx,
                                         val postRepository: PostRepository,
                                         val commentRepository: CommentRepository): Controller({

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

    route("/view/:postId").handler{ req ->
        val postId = req.request().getParam("postId")

        postRepository.getPost(postId).subscribe(
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

    route("/:postId/comments").handler{ req ->
        val postId = req.request().getParam("postId")
        val cursor = req.request().getParam("cursor")
        val count = req.request().getParam("count") ?: "5"

        commentRepository.getCommentsByPost(postId, cursor, count.toInt())
                .subscribe(
                        { result ->
                            val finalResult = result.map{r -> r.toJson()}
                            req.response()
                                    .putHeader("content-type", "application/json")
                                    .end(Json.encode(finalResult))
                        },
                        { err ->
                            req.response()
                                    .putHeader("content-type", "text/html")
                                    .end(err.toString())
                        }
                )
    }

    route(HttpMethod.POST, "/add").handler(MustAuthenticateHandler)
    route(HttpMethod.POST, "/add").handler{ req ->
        req.request().isExpectMultipart = true
        req.next()
    }
    route(HttpMethod.POST, "/add").handler(
            BodyHandler.create()
                       .setMergeFormAttributes(true)
    )
    route(HttpMethod.POST, "/add").handler{ req ->
        fun getExtension(filename: String): String {
            return filename.split(".").last()
        }

        val formData = req.request().formAttributes()
        val uploadPath = DiskursusConfiguration.UploadURLPath
        val uploads = req.fileUploads()
                         .toList()
                         .filter{ upload -> upload.contentType().contains("image") }
                         .map{ upload ->
                             "$uploadPath${upload.uploadedFileName()}.${getExtension(upload.fileName())}"
                         }

        try {
            for(upload in req.fileUploads()) {
                val finalFileName = ".$uploadPath${upload.uploadedFileName()}.${getExtension(upload.fileName())}"
                vertx.fileSystem().copyBlocking(upload.uploadedFileName(), finalFileName)
                vertx.fileSystem().deleteBlocking(upload.uploadedFileName())
            }
        } catch(exception: Exception) {
            req.response()
               .putHeader("content-type", "text/html")
               .end(exception.message)
        }

        val newPost = Post(
                _id = "",
                content = formData.get("content"),
                poster = req.session().get<User>(DiskursusConfiguration.UserInfoSessionKey),
                pictures = uploads
        )

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