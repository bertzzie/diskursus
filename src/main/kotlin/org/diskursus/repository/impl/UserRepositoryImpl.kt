package org.diskursus.repository.impl

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

    override fun getUsers(page: Int, perPage: Int): List<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserData(id: Long): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addUser(user: User): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUser(user: User): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeUser(user: User): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}