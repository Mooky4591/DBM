package com.example.dbm.login.presentation

import com.example.dbm.presentation.UiText

sealed interface LoginEvents {
    data class OnEmailChanged(val email: String) : LoginEvents
    data class OnPasswordChanged(val password: String) : LoginEvents
    data class OnTogglePasswordVisibility(val isPasswordVisible: Boolean) : LoginEvents
    data class OnNameChanged(val name: String) : LoginEvents
    data object OnLoginClick : LoginEvents
    data object OnRegisterLinkClick : LoginEvents
    data class LoginFailed(val errorText: UiText) : LoginEvents
    data class LoginSuccess(val email: String) : LoginEvents
}