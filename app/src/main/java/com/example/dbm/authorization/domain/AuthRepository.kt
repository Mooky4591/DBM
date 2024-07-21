package com.example.dbm.authorization.domain

import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.login.domain.objects.Login
import com.example.dbm.login.domain.objects.User


interface AuthRepository {
    fun isEmailValid(email: String): Boolean
    suspend fun loginUser(login: Login): Result<User, DataError.Network>
    suspend fun registerUser(user: User): Result<User, DataError.Network>
}
