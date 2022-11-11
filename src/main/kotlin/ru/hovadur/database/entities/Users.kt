package ru.hovadur.database.entities

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val phone = Users.varchar("phone", 25)
    val email = Users.varchar("email", 25).nullable()
    val username = Users.varchar("username", 50).nullable()
    val irkktSessionId = Users.varchar("irkkt_session_id", 70).nullable()
    val irkktRefreshToken = Users.varchar("irkkt_refresh_token", 70).nullable()
    override val primaryKey = PrimaryKey(phone)
}