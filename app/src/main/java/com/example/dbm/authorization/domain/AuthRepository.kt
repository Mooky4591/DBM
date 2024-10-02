package com.example.dbm.authorization.domain

import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.login.presentation.objects.Login
import com.example.dbm.login.presentation.objects.User


interface AuthRepository {
    fun isEmailValid(email: String): Boolean
    suspend fun loginUser(login: Login): Result<User, DataError.Network>
    suspend fun registerUser(user: User, password: String): Result<User, DataError.Network>
    suspend fun updatePassword(email: String, password: String): Result<Boolean, DataError.Network>
}
