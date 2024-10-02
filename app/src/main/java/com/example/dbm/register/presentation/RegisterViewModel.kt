package com.example.dbm.register.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.error_handling.domain.asUiText
import com.example.dbm.login.presentation.objects.User
import com.example.dbm.presentation.UiText
import com.example.dbm.register.domain.use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set
    private val eventChannel = Channel<RegisterEvents>()
    val event = eventChannel.receiveAsFlow()

    fun onEvent(event: RegisterEvents) {
        when (event) {
            is RegisterEvents.OnFirstNameChanged -> state = state.copy(firstName = event.name)
            is RegisterEvents.OnLastNameChanged -> state = state.copy(lastName = event.name)
            is RegisterEvents.OnEmailChanged -> state = state.copy(
                email = event.email,
                isEmailValid = validateEmail(event.email)
            )
            is RegisterEvents.OnPasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is RegisterEvents.OnGetStartedClick -> register(event.login, event.password)
            is RegisterEvents.OnTogglePasswordVisibility -> state = state.copy(isPasswordVisible = event.isPasswordVisible)
            is RegisterEvents.RegistrationSuccessful -> {}
            is RegisterEvents.RegistrationFailed -> {}
            is RegisterEvents.OnPhoneNumberChanged -> state = state.copy(phoneNumber = event.phoneNumber)
            is RegisterEvents.OnCompanyAddressChanged -> state = state.copy(companyAddress = event.address)
            is RegisterEvents.OnCompanyNameChanged -> state = state.copy(companyName = event.companyName)
        }
    }

    private fun register(user: User, password: String) {
        viewModelScope.launch {
            when (validatePassword(password)) {
                true -> {
                    when (validateEmail(user.email)) {
                        true -> {
                            when (val result = registerUseCase.registerUser(user, password)) {
                                is Result.Success -> {
                                    state = state.copy(isRegistrationSuccessful = true)
                                    eventChannel.send(RegisterEvents.RegistrationSuccessful)
                                }
                                is Result.Error -> {
                                    state = state.copy(isRegistrationSuccessful = false)
                                    state = state.copy(networkErrorMessage = result.error.asUiText())
                                    eventChannel.send(RegisterEvents.RegistrationFailed(state.networkErrorMessage!!))
                                }
                            }
                        }
                        false -> {
                            state = state.copy(isRegistrationSuccessful = false)
                            eventChannel.send(RegisterEvents.RegistrationFailed(state.invalidEmail!!))
                        }
                    }
                }
                false -> {
                    state = state.copy(isRegistrationSuccessful = false)
                    eventChannel.send(RegisterEvents.RegistrationFailed(state.passwordInvalidErrorMessage!!))
                }
            }
        }
    }

    private fun validateEmail(email: String) : Boolean {
        return when(val result = registerUseCase.isEmailValid(email)) {
            is Result.Error -> {
                state = state.copy(
                    invalidEmail = result.error.asUiText()
                )
                false
            }
            is Result.Success -> {
                true
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        return when (val result = registerUseCase.isPasswordValid(password)) {
            is Result.Error -> {
                val error = result.error.asUiText()
                state = state.copy(
                    passwordInvalidErrorMessage = error
                )
                false
            }

            is Result.Success -> {
                state = state.copy(passwordInvalidErrorMessage = null)
                true
            }
       }
    }
}

data class RegisterState(
    var email: String? = null,
    var password: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var phoneNumber: String? = null,
    var userId: String? = null,
    var companyAddress: String? = null,
    var companyName: String? = null,
    var isEmailValid: Boolean = false,
    var isPasswordVisible: Boolean = false,
    var isRegistrationSuccessful: Boolean = false,
    var isLoginSuccessful: Boolean = false,
    var isLoading: Boolean = false,
    var passwordInvalidErrorMessage: UiText? = null,
    var invalidEmail: UiText? = null,
    var networkErrorMessage: UiText? = null
)