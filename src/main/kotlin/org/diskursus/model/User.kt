package org.diskursus.model

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.ext.auth.AbstractUser
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
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
data class User(val name: String,
                val email: String,
                val password: String,
                val picture: String,
                val status: UserStatus,
                val role: UserRole,
                val lastLogin: DateTime?,
                val registerTime: DateTime): AbstractUser() {
    companion object {
        fun getDefaultUser(): User = User(
                "admin",
                "admin@diskursus.org",
                BCrypt.hashpw("admin", BCrypt.gensalt()),
                "",
                UserStatus.ACTIVE,
                UserRole.ADMIN,
                null,
                DateTime.now()
        )

        fun fromJson(obj: JsonObject): User = User(
                name = obj.getString("name"),
                email = obj.getString("email"),
                password = obj.getString("password"),
                picture = obj.getString("picture"),
                status = UserStatus.fromString(obj.getString("status")),
                role = UserRole.fromString(obj.getString("role")),
                lastLogin = if(obj.getString("lastLogin") == null) null else DateTime.parse(obj.getString("lastLogin")),
                registerTime = DateTime.parse(obj.getString("registerTime"))
        )
    }

    override fun principal(): JsonObject {
        return this.toJson()
    }

    override fun setAuthProvider(authProvider: AuthProvider?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun doIsPermitted(permission: String?, resultHandler: Handler<AsyncResult<Boolean>>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun toJson(): JsonObject = json {
        obj(
                "name" to name,
                "email" to email,
                "password" to password,
                "picture" to picture,
                "status" to status.toString(),
                "role" to role.toString(),
                "lastLogin" to lastLogin?.toString(),
                "registerTime" to registerTime.toString()
        )
    }
}
