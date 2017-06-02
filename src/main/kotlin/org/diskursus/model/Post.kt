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
                val poster: String,
                val pictures: List<String> = listOf(),
                val createdAt: DateTime = DateTime.now(),
                val updatedAt: DateTime = DateTime.now()) {
    companion object {
        fun fromJson(json: JsonObject): Post = Post(
                _id = json.getString("_id"),
                content = json.getString("content"),
                poster = json.getString("poster"),
                pictures = json.getJsonArray("pictures").map{ it.toString() },
                createdAt = DateTime.parse(json.getString("createdAt")),
                updatedAt = DateTime.parse(json.getString("updatedAt"))
        )
    }

    fun toJson(): JsonObject = json {
        obj(
                "_id" to _id,
                "content" to content,
                "poster" to poster,
                "pictures" to pictures,
                "createdAt" to createdAt.toString(),
                "updatedAt" to updatedAt.toString()
        )
    }

    fun toJsonWithoutId(): JsonObject = json {
        obj(
                "content" to content,
                "poster" to poster,
                "pictures" to pictures,
                "createdAt" to createdAt.toString(),
                "updatedAt" to updatedAt.toString()
        )
    }
}
