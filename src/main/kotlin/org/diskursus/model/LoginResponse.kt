package org.diskursus.model

import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
data class LoginResponse(val success: Boolean,
                         val user: User? = null) {
    fun toJson(): JsonObject = json {
        obj(
                "success" to success.toString(),
                "user" to user?.toJson()
        )
    }
}