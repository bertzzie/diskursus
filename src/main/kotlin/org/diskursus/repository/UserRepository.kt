package org.diskursus.repository

import org.diskursus.model.User
import rx.Observable
import rx.Single

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
interface UserRepository {
    fun getAllUsers(): Single<List<User>>
    fun getUsers(page: Int = 1, perPage: Int = 10): List<User>
    fun getUserData(id: Long): User

    fun addUser(user: User): Boolean
    fun updateUser(user: User): Boolean
    fun removeUser(user: User): Boolean
}