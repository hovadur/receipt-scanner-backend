package ru.hovadur.route.v1.auth.data

import ru.hovadur.database.dao.UsersDao
import ru.hovadur.route.v1.auth.data.dto.LoginResp
import ru.hovadur.route.v1.auth.data.dto.RegisterResp
import ru.hovadur.route.v1.auth.data.dto.RegisterResult
import ru.hovadur.route.v1.auth.domain.model.User

class UserController(private val jwtConfig: JwtConfig) {

    suspend fun isValid(value: LoginResp): Boolean {
        val userDto = UsersDao.fetch(value.login)
        return if (userDto == null) {
            false
        } else {
            userDto.pass == jwtConfig.hash(value.password)
        }
    }

    suspend fun insert(value: RegisterResp): RegisterResult {
        val userDto = UsersDao.fetch(value.login)
        return if (userDto == null) {
            UsersDao.insert(
                User(
                    login = value.login,
                    pass = jwtConfig.hash(value.password),
                    email = value.email,
                    username = value.username
                )
            )
            RegisterResult.Success
        } else RegisterResult.Duplicate
    }
}