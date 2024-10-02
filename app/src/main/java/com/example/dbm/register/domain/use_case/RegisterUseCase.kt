package com.example.dbm.register.domain.use_case

import com.example.dbm.authorization.domain.AuthRepository
import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.EmailValidator
import com.example.dbm.error_handling.domain.PasswordValidator
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.login.presentation.objects.User
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val passwordValidator: PasswordValidator,
    private val emailValidator: EmailValidator
) {
    suspend fun registerUser(user: User, password: String): Result<User, DataError.Network> {
        return authRepository.registerUser(user, password)
    }

    fun isEmailValid(email: String): Result<*, EmailValidator.EmailError> {
        return emailValidator.validateEmail(email)
    }

    fun isPasswordValid(password: String): Result<*, PasswordValidator.PasswordError> {
        return passwordValidator.validatePassword(password)
    }

    suspend fun updatePassword(email: String, password: String): Result<Boolean, DataError.Network> {
        return authRepository.updatePassword(email, password)
    }
}