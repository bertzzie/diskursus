package org.diskursus.repository.impl

import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.mongo.MongoClientDeleteResult
import io.vertx.ext.mongo.MongoClientUpdateResult
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.diskursus.ext.single
import org.diskursus.model.User
import org.diskursus.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt
import rx.Single
import java.lang.NullPointerException
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

    override fun getUserData(name: String): Single<User?> {
        val result = single<JsonObject> {
            client.findOne(docName, json { obj ( "name" to name ) }, null, it)
        }

        return result.map { r: JsonObject? ->
            if (r == null) null
            else User.fromJson(r)
        }
    }

    override fun getUserDataFromID(id: String): Single<User?> {
        val result = single<JsonObject> {
            client.findOne(docName, json { obj ( "_id" to id ) }, null, it)
        }

        return result.map{ r: JsonObject? ->
            if (r == null) null
            else User.fromJson(r)
        }
    }

    override fun addUser(user: User): Single<String> {
        return single<String> {
            client.insert(docName, user.toJson(), it)
        }
    }

    override fun updateUser(user: User): Single<Boolean> {
        val query = json { obj( "name" to user.name ) }
        val update = json {
            obj("\$set" to user.toJson())
        }

        val result = single<MongoClientUpdateResult> { client.updateCollection(docName, query, update, it) }

        return result.map { r -> r.docModified == 1L } // only 1 doc should be modified!
    }

    override fun removeUser(name: String): Single<Boolean> {
        val result = single<MongoClientDeleteResult> {
            client.removeDocument(docName, json { obj( "name" to name ) }, it)
        }

        return result.map{ r -> r.removedCount == 1L }
    }

    override fun authenticate(name: String, password: String): Single<User?> {
        return getUserData(name).map{ user ->
            if (user == null) {
                null
            } else if (BCrypt.checkpw(password, user.password.orEmpty())) user
            else null
        }
    }
}