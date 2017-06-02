package org.diskursus.repository.impl

import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.mongo.MongoClientDeleteResult
import io.vertx.ext.mongo.MongoClientUpdateResult
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.diskursus.ext.single
import org.diskursus.model.Post
import org.diskursus.repository.PostRepository
import rx.Single
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class PostRepositoryImpl @Inject constructor(val client: MongoClient): PostRepository {
    val docName = "posts"

    override fun getPosts(cursor: String, perPage: Int): Single<List<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPost(id: String): Single<Post> {
        val result = single<JsonObject> { client.findOne(docName, json { obj("_id" to id) }, null, it) }

        return result.map{ r -> Post.fromJson(r) }
    }

    override fun createPost(post: Post): Single<String> {
        return single<String> {
            client.insert(docName, post.toJsonWithoutId(), it)
        }
    }

    override fun updatePost(post: Post): Single<Boolean> {
        val query = json { obj("_id" to post._id) }
        val update = json {
            obj("\$set" to post.toJson())
        }

        val result = single<MongoClientUpdateResult> { client.updateCollection(docName, query, update, it) }

        return result.map { r -> r.docModified == 1L } // only 1 doc should be modified!
    }

    override fun deletePost(id: String): Single<Boolean> {
        val result = single<MongoClientDeleteResult> {
            client.removeDocument(docName, json { obj( "_id" to id ) }, it)
        }

        return result.map{ r -> r.removedCount == 1L }
    }
}