package org.diskursus.repository

import org.diskursus.model.Comment
import rx.Single

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
interface CommentRepository {
    fun getComment(id: String): Single<Comment>
    fun getCommentsByPost(postId: String, cursor: String?, count: Int = 5): Single<List<Comment>>
    fun getCommentCountByPost(postId: String): Single<Long>

    fun createComment(comment: Comment): Single<String>
    fun updateComment(comment: Comment): Single<Boolean>
    fun deleteComment(id: String): Single<Boolean>
}