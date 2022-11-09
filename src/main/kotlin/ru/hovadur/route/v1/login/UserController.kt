package ru.hovadur.route.v1.login

import ru.hovadur.JwtConfig
import ru.hovadur.database.UserDTO
import ru.hovadur.database.Users
import ru.hovadur.route.v1.model.LoginResp
import ru.hovadur.route.v1.model.RegisterResp
import ru.hovadur.route.v1.model.RegisterResult

class UserController(private val jwtConfig: JwtConfig) {

    suspend fun isValid(value: LoginResp): Boolean {
        val userDto = Users.fetch(value.login)
        return if (userDto == null) {
            false
        } else {
            userDto.pass == jwtConfig.hash(value.password)
        }
    }

    suspend fun insert(value: RegisterResp): RegisterResult {
        val userDto = Users.fetch(value.login)
        return if (userDto == null) {
            Users.insert(
                UserDTO(
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