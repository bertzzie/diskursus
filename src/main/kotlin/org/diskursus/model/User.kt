package org.diskursus.model

import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.diskursus.ext.toFullDateTimeString
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
data class User(val id: Long,
                val name: String,
                val email: String,
                val password: String,
                val picture: String,
                val status: UserStatus,
                val role: UserRole,
                val lastLogin: DateTime?,
                val registerTime: DateTime) {
    companion object {
        fun getDefaultUser(): User = User(
                1,
                "admin",
                "admin@diskursus.org",
                BCrypt.hashpw("admin", BCrypt.gensalt()),
                "",
                UserStatus.ACTIVE,
                UserRole.ADMIN,
                null,
                DateTime.now()
        )
    }

    fun toJson(): JsonObject = json {
        obj(
                "id" to id,
                "name" to name,
                "email" to email,
                "picture" to picture,
                "status" to status.toString(),
                "role" to role.toString(),
                "lastLogin" to lastLogin?.toFullDateTimeString(),
                "registerTime" to registerTime.toFullDateTimeString()
        )
    }
}
