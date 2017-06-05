package org.diskursus.model

import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
data class UserInfo(val isLoggedIn: Boolean,
                    val name: String?,
                    val email: String?,
                    val picture: String?,
                    val role: String?) {
    fun toJson(): JsonObject = json {
        obj(
                "isLoggedIn" to isLoggedIn,
                "name" to name,
                "email" to email,
                "picture" to picture,
                "role" to role
        )
    }
}