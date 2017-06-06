package org.diskursus.repository.impl

import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.mongo.MongoClientDeleteResult
import io.vertx.ext.mongo.MongoClientUpdateResult
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.ext.mongo.FindOptions
import org.diskursus.ext.single
import org.diskursus.model.Comment
import org.diskursus.repository.CommentRepository
import rx.Single
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
class CommentRepositoryImpl @Inject constructor(val client: MongoClient): CommentRepository {
    val docName = "comments"

    override fun getComment(id: String): Single<Comment> {
        val result = single<JsonObject> { client.findOne(docName, json { obj("_id" to id) }, null, it) }

        return result.map{ r -> Comment.fromJson(r) }
    }

    override fun getCommentsByPost(postId: String, cursor: String?, count: Int): Single<List<Comment>> {
        val options = FindOptions(null, count, null, json { obj("updatedAt" to -1) })
        val query = if (cursor == null) {
            json { obj( "postId" to postId ) }
        } else {
            json{
                obj(
                        "_id" to obj("\$lt" to cursor), // less than because we always sort descending
                        "postId" to postId
                )
            }
        }

        val result = single<List<JsonObject>> {
            client.findWithOptions(docName, query, options, it)
        }

        return result.map{ r -> r.map{ Comment.fromJson(it) } }
    }

    override fun createComment(comment: Comment): Single<String> {
        return single<String> {
            client.insert(docName, comment.toMongoJson(), it)
        }
    }

    override fun updateComment(comment: Comment): Single<Boolean> {
        val query = json { obj("_id" to comment._id) }
        val update = json {
            obj("\$set" to comment.toFullMongoJson())
        }

        val result = single<MongoClientUpdateResult> { client.updateCollection(docName, query, update, it) }

        return result.map { r -> r.docModified == 1L } // only 1 doc should be modified!
    }

    override fun deleteComment(id: String): Single<Boolean> {
        val result = single<MongoClientDeleteResult> {
            client.removeDocument(docName, json { obj( "_id" to id ) }, it)
        }

        return result.map{ r -> r.removedCount == 1L }
    }
}