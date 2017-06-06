package org.diskursus.model

import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.joda.time.DateTime

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
data class Comment(val _id: String,
                   val postId: String,
                   val content: String,
                   val poster: User,
                   val createdAt: DateTime = DateTime.now(),
                   val updatedAt: DateTime = DateTime.now()) {
    companion object {
        fun fromJson(json: JsonObject): Comment {
            val createdAt = if(json.getString("createdAt") == null) {
                DateTime.now()
            } else {
                DateTime.parse(json.getString("createdAt"))
            }

            val updatedAt = if(json.getString("updatedAt") == null) {
                DateTime.now()
            } else {
                DateTime.parse(json.getString("updatedAt"))
            }

            return Comment(_id = if (json.getString("_id") == null) "" else json.getString("_id"),
                           postId = json.getString("postId").orEmpty(),
                           content = json.getString("content").orEmpty(),
                           poster = User.fromClientJson(json.getJsonObject("poster")),
                           createdAt = createdAt,
                           updatedAt = updatedAt)
        }
    }

    fun toJson(): JsonObject = json {
        obj(
                "_id" to _id,
                "postId" to postId,
                "content" to content,
                "poster" to poster.toPublicJson(),
                "createdAt" to createdAt.millis / 1000,
                "updatedAt" to updatedAt.millis / 1000
        )
    }

    fun toFullMongoJson(): JsonObject = json {
        obj(
                "_id" to _id,
                "postId" to postId,
                "content" to content,
                "poster" to poster.toPublicJson(),
                "createdAt" to createdAt.toString(),
                "updatedAt" to updatedAt.toString()
        )
    }

    fun toMongoJson(): JsonObject = json {
        obj(
                "postId" to postId,
                "content" to content,
                "poster" to poster.toPublicJson(),
                "createdAt" to createdAt.toString(),
                "updatedAt" to updatedAt.toString()
        )
    }
}