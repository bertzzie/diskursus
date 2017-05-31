package org.diskursus.model

/**
 * [Documentation Here]
 *
 * @author Alex Xandra Albert Sim
 */
enum class UserStatus {
    ACTIVE,
    INACTIVE,
    BANNED;

    companion object {
        fun fromString(str: String): UserStatus = when (str.toLowerCase()) {
            "active" -> ACTIVE
            "inactive" -> INACTIVE
            "banned" -> BANNED
            else -> throw IllegalArgumentException("Unknown User Status: ${str}")
        }
    }
}