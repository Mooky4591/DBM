package com.example.dbm.register.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dbm.presentation.UiText
import com.example.dbm.register.domain.use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.UUID
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
                isEmailValid = registerUseCase.isEmailValid(event.email)
            )

            is RegisterEvents.OnPasswordChanged -> state = state.copy(password = event.password)
            is RegisterEvents.OnGetStartedClick -> register()
            is RegisterEvents.OnTogglePasswordVisibility -> state =
                state.copy(isPasswordVisible = event.isPasswordVisible)

            is RegisterEvents.RegistrationSuccessful -> {}
            is RegisterEvents.RegistrationFailed -> {}
            is RegisterEvents.OnPhoneNumberChanged -> state = state.copy(phoneNumber = event.phoneNumber)
        }
    }

    private fun register() {
        TODO("Not yet implemented")
    }
}

data class RegisterState(
    var email: String? = null,
    var password: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var phoneNumber: String? = null,
    var userId: UUID? = null,
    var isEmailValid: Boolean = false,
    var isPasswordVisible: Boolean = false,
    var isRegistrationSuccessful: Boolean = false,
    var isLoginSuccessful: Boolean = false,
    var isLoading: Boolean = false,
    var passwordInvalidErrorMessage: UiText? = null,
    var networkErrorMessage: UiText? = null
)