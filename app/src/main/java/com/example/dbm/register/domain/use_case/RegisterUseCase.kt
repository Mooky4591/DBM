package com.example.dbm.register.domain.use_case

import android.util.Patterns
import com.example.dbm.authorization.domain.AuthRepository
import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.PasswordValidator
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.login.presentation.objects.User
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val passwordValidator: PasswordValidator
) {
    suspend fun registerUser(user: User): Result<User, DataError.Network> {
        return authRepository.registerUser(user)
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Result<*, PasswordValidator.PasswordError> {
        return passwordValidator.validatePassword(password)
    }
}