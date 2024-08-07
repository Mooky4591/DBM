package com.example.dbm.login.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbm.authorization.domain.AuthRepository
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.error_handling.domain.PasswordValidator
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.error_handling.domain.asUiText
import com.example.dbm.login.presentation.objects.Login
import com.example.dbm.login.domain.use_case.LoginUseCase
import com.example.dbm.presentation.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val passwordValidator: PasswordValidator,
    private val loginUseCase: LoginUseCase,
    private val userPrefs: UserPreferences
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvents>()
    val events = eventChannel.receiveAsFlow()

    init {
        state = state.copy(isReady = false)
    }

    fun onEvent(event: LoginEvents) {
        when (event) {
            is LoginEvents.LoginFailed -> {/*handled in the navigation class*/}
            is LoginEvents.LoginSuccess -> {/*handled in the navigation class*/}
            is LoginEvents.OnRegisterLinkClick -> {/*handled in the navigation class*/}
            is LoginEvents.OnEmailChanged ->
                state = state.copy(
                email = event.email,
                isEmailValid = authRepository.isEmailValid(event.email)
            )
            is LoginEvents.OnLoginClick -> {
                login()
            }
            is LoginEvents.OnNameChanged ->
                state = state.copy(
                    name = event.name
                )
            is LoginEvents.OnPasswordChanged -> {
                passwordValidator.validatePassword(event.password)
                state = state.copy(password = event.password)
            }
            is LoginEvents.OnTogglePasswordVisibility ->
                state = state.copy(
                    isPasswordVisible = event.isPasswordVisible
                )
        }
    }

    private fun login() {
        val login = Login(email = state.email, password = state.password)
        if (state.isEmailValid) {
            viewModelScope.launch {
                state = state.copy(isLoggingIn = true)
                when (val loginResponse = loginUseCase.loginUser(login)) {
                    is Result.Success -> {
                        state = state.copy(isLoggingIn = false)
                        userPrefs.addUserEmail(login.email)
                        eventChannel.send(LoginEvents.LoginSuccess(login.email))
                    }

                    is Result.Error -> {
                        state = state.copy(loginErrorMessage = loginResponse.error.asUiText())
                        state = state.copy(isLoggingIn = false)
                        eventChannel.send(LoginEvents.LoginFailed(state.loginErrorMessage!!))
                    }
                }
            }
        }
    }
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isLoggingIn: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val loginErrorMessage: UiText? = null,
    val isReady: Boolean = true

)