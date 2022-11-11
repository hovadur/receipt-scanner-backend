package ru.hovadur.database.dao

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import ru.hovadur.database.DatabaseFactory
import ru.hovadur.database.entities.Users
import ru.hovadur.route.v1.auth.domain.model.User

object UsersDao {

    suspend fun insert(value: User) {
        DatabaseFactory.dbQuery {
            Users.insert {
                it[phone] = value.phone
                it[email] = value.email
                it[username] = value.name
                it[irkktSessionId] = value.irkktSessionId
                it[irkktRefreshToken] = value.irkktRefreshToken
            }
        }
    }

    suspend fun update(phone: String, sessionId: String, refreshToken: String) = DatabaseFactory.dbQuery {
        Users.update(where = { Users.phone.eq(phone) }) {
            it[irkktSessionId] = sessionId
            it[irkktRefreshToken] = refreshToken
        }
    }

    suspend fun fetch(phone: String): User? = DatabaseFactory.dbQuery {
        Users.select { Users.phone.eq(phone) }.firstOrNull()?.let { value ->
            User(
                phone = value[Users.phone],
                email = value[Users.email],
                name = value[Users.username],
                irkktSessionId = value[Users.irkktSessionId],
                irkktRefreshToken = value[Users.irkktRefreshToken]
            )
        }
    }
}