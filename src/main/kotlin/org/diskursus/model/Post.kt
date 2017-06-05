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
data class Post(val _id: String,
                val content: String,
                val poster: User,
                val pictures: List<String> = listOf(),
                val createdAt: DateTime = DateTime.now(),
                val updatedAt: DateTime = DateTime.now()) {
    companion object {
        fun fromJson(json: JsonObject): Post {
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

            val pictures = if (json.getJsonArray("pictures") == null) {
                listOf()
            } else {
                json.getJsonArray("pictures").map{ it.toString() }
            }

            return Post(_id = if (json.getString("_id") == null) "" else json.getString("_id"),
                        content = json.getString("content"),
                        poster = User.fromClientJson(json.getJsonObject("poster")),
                        pictures = pictures,
                        createdAt = createdAt,
                        updatedAt = updatedAt)
        }
    }

    fun toJson(): JsonObject = json {
        obj(
                "_id" to _id,
                "content" to content,
                "poster" to poster.toPublicJson(),
                "pictures" to pictures,
                "createdAt" to createdAt.toString(),
                "updatedAt" to updatedAt.toString()
        )
    }

    fun toJsonWithoutId(): JsonObject = json {
        obj(
                "content" to content,
                "poster" to poster.toPublicJson(),
                "pictures" to pictures,
                "createdAt" to createdAt.toString(),
                "updatedAt" to updatedAt.toString()
        )
    }
}
