package org.diskursus.repository

import org.diskursus.model.Post
import rx.Single

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
interface PostRepository {
    fun getPosts(cursor: String, perPage: Int = 5): Single<List<Post>>
    fun getPost(id: String): Single<Post>

    fun createPost(post: Post): Single<String>
    fun updatePost(post: Post): Single<Boolean>
    fun deletePost(id: String): Single<Boolean>
}