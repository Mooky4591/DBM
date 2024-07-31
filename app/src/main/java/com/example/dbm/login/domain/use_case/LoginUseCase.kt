package com.example.dbm.login.domain.use_case

import com.example.dbm.authorization.domain.AuthRepository
import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.login.presentation.objects.Login
import com.example.dbm.login.presentation.objects.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun loginUser(login: Login): Result<User, DataError.Network> {
        return authRepository.loginUser(login)
    }
}