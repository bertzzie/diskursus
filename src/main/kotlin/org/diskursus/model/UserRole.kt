package org.diskursus.model

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
enum class UserRole {
    ADMIN,
    MODERATOR,
    STANDARD;

    companion object {
        fun fromString(str: String): UserRole = when (str.toLowerCase()) {
            "admin" -> ADMIN
            "moderator" -> MODERATOR
            "standard" -> STANDARD
            else -> throw IllegalArgumentException("Unknown user role: ${str}")
        }
    }
}