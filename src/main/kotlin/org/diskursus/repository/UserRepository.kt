package org.diskursus.repository

import org.diskursus.model.User
import rx.Single

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
interface UserRepository {
    fun getAllUsers(): Single<List<User>>
    fun getUsers(page: Int = 1, perPage: Int = 10): Single<List<User>>
    fun getUserData(name: String): Single<User>

    fun addUser(user: User): Single<String>
    fun updateUser(user: User): Single<Boolean>
    fun removeUser(name: String): Single<Boolean>
}