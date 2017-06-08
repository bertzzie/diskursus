package org.diskursus.model

import io.vertx.core.json.JsonObject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
data class Register(val username: String,
                    val password: String,
                    val retypePassword: String) {
    companion object {
        fun validate(data: Register): Boolean {
            return data.password == data.retypePassword
        }
    }
}