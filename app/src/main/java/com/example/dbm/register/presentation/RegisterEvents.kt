package com.example.dbm.register.presentation

import com.example.dbm.login.domain.objects.Login
import com.example.dbm.login.domain.objects.User
import com.example.dbm.presentation.UiText

interface RegisterEvents {
    data class OnFirstNameChanged(val name: String) : RegisterEvents
    data class OnLastNameChanged(val name: String) : RegisterEvents
    data class OnEmailChanged(val email: String) : RegisterEvents
    data class OnPasswordChanged(val password: String) : RegisterEvents
    data class OnPhoneNumberChanged(val phoneNumber: String) : RegisterEvents
    data class OnTogglePasswordVisibility(val isPasswordVisible: Boolean) : RegisterEvents
    data class OnGetStartedClick(val login: User) : RegisterEvents
    data object RegistrationSuccessful : RegisterEvents
    data class RegistrationFailed(val errorMessage: UiText) : RegisterEvents
}