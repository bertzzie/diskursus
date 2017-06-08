package org.diskursus.controller

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class MainController @Inject constructor(override val router: Router,
                                         override val vertx: Vertx,
                                         val userController: UserController,
                                         val postController: PostController,
                                         val commentController: CommentController): Controller({
    mountSubRouter("/user/", userController.create())
    mountSubRouter("/post/", postController.create())
    mountSubRouter("/comment/", commentController.create())
})