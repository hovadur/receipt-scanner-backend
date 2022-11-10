package ru.hovadur.database.entities

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val phone = Users.varchar("phone", 25)
    val email = Users.varchar("email", 25).nullable()
    val name = Users.varchar("name", 50).nullable()
    val irkktSessionId = Users.varchar("irkktSessionId", 70).nullable()
    val irkktRefreshToken = Users.varchar("irkktRefreshToken", 70).nullable()
    override val primaryKey = PrimaryKey(phone)
}