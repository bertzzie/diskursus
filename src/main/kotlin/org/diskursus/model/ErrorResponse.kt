package org.diskursus.model

import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
data class ErrorResponse(val cause: String,
                         val code: String) {
    fun toJson(): JsonObject = json {
        obj(
                "cause" to cause,
                "code" to code
        )
    }
}