package ru.hovadur.database.entities

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val login = Users.varchar("login", 25)
    val pass = Users.varchar("pass", 50)
    val email = Users.varchar("email", 25).nullable()
    val username = Users.varchar("username", 50).nullable()
    override val primaryKey = PrimaryKey(login)
}