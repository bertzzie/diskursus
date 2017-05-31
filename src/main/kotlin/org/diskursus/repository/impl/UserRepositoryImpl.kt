package org.diskursus.repository.impl

import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.diskursus.ext.single
import org.diskursus.model.User
import org.diskursus.repository.UserRepository
import rx.Single
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */

class UserRepositoryImpl @Inject constructor(val client: MongoClient) : UserRepository {
    val docName = "users"

    override fun getAllUsers(): Single<List<User>> {
        val result = single<List<JsonObject>> { client.find(docName, json { obj() }, it) }
        return result.map { r -> r.map { u -> User.fromJson(u) } }
    }

    override fun getUsers(page: Int, perPage: Int): Single<List<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserData(name: String): Single<User> {
        val result = single<JsonObject> {
            client.findOne(docName, json { obj ( "name" to name ) }, null, it)
        }

        return result.map { r -> User.fromJson(r) }
    }

    override fun addUser(user: User): Single<String> {
        return single<String> {
            client.insert(docName, user.toJson(), it)
        }
    }

    override fun updateUser(user: User): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeUser(user: User): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}