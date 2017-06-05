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
data class FullPost(val _id: String,
                    val content: String,
                    val poster: User,
                    val pictures: List<String> = listOf(),
                    val createdAt: DateTime = DateTime.now(),
                    val updatedAt: DateTime = DateTime.now()) {
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
}