package com.example.dbm.register.presentation

import com.example.dbm.login.presentation.objects.User
import com.example.dbm.presentation.UiText

interface RegisterEvents {
    data class OnFirstNameChanged(val name: String) : RegisterEvents
    data class OnLastNameChanged(val name: String) : RegisterEvents
    data class OnEmailChanged(val email: String) : RegisterEvents
    data class OnPasswordChanged(val password: String) : RegisterEvents
    data class OnPhoneNumberChanged(val phoneNumber: String) : RegisterEvents
    data class OnCompanyAddressChanged(val address: String) : RegisterEvents
    data class OnCompanyNameChanged(val companyName: String): RegisterEvents
    data class OnTogglePasswordVisibility(val isPasswordVisible: Boolean) : RegisterEvents
    data class OnGetStartedClick(val login: User, val password: String) : RegisterEvents
    data object RegistrationSuccessful : RegisterEvents
    data class RegistrationFailed(val errorMessage: UiText) : RegisterEvents
}