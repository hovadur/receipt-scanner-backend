package ru.hovadur.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.hovadur.DatabaseFactory.dbQuery

object Users : Table() {
    private val login = Users.varchar("login", 25)
    private val pass = Users.varchar("pass", 50)
    private val email = Users.varchar("email", 25).nullable()
    private val username = Users.varchar("username", 50).nullable()
    override val primaryKey = PrimaryKey(login)
    suspend fun insert(value: UserDTO) {
        dbQuery {
            Users.insert {
                it[login] = value.login
                it[pass] = value.pass
                it[email] = value.email
                it[username] = value.username
            }
        }
    }

    suspend fun fetch(login: String): UserDTO? = dbQuery {
        Users.select { Users.login.eq(login) }.firstOrNull()?.let { value ->
            UserDTO(login = value[Users.login], pass = value[pass], email = value[email], username = value[username])
        }
    }
}