package com.example.dbm.presentation.edit_text.domain

import com.example.dbm.error_handling.domain.EmailValidator
import com.example.dbm.error_handling.domain.PasswordValidator
import com.example.dbm.error_handling.domain.Result
import javax.inject.Inject

class ValidateUserInfoUseCase @Inject constructor(
    private val passwordValidator: PasswordValidator,
    private val emailValidator: EmailValidator
) {

    fun isEmailValid(email: String): Result<*, EmailValidator.EmailError> {
        return emailValidator.validateEmail(email)
    }

    fun isPasswordValid(password: String): Result<*, PasswordValidator.PasswordError> {
        return passwordValidator.validatePassword(password)
    }
}