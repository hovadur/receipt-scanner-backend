package ru.hovadur.database.dao

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.hovadur.database.DatabaseFactory
import ru.hovadur.database.entities.Users
import ru.hovadur.route.v1.auth.domain.model.User

object UsersDao {

    suspend fun insert(value: User) {
        DatabaseFactory.dbQuery {
            Users.insert {
                it[login] = value.login
                it[pass] = value.pass
                it[email] = value.email
                it[username] = value.username
            }
        }
    }

    suspend fun fetch(login: String): User? = DatabaseFactory.dbQuery {
        Users.select { Users.login.eq(login) }.firstOrNull()?.let { value ->
            User(
                login = value[Users.login],
                pass = value[Users.pass],
                email = value[Users.email],
                username = value[Users.username]
            )
        }
    }
}