package com.example.dbm.presentation.edit_text.presentation

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbm.R
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.error_handling.domain.asUiText
import com.example.dbm.login.presentation.objects.User
import com.example.dbm.presentation.edit_text.domain.EditTextRepository
import com.example.dbm.presentation.edit_text.domain.ValidateUserInfoUseCase
import com.example.dbm.presentation.edit_text.enum.EditTextType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTextViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val editTextRepo: EditTextRepository,
    private val validatorUseCase: ValidateUserInfoUseCase
) : ViewModel() {
    var state by mutableStateOf(EditTextState(
        userId = userPreferences.getUserId()
    ))
        private set

    private val eventChannel = MutableSharedFlow<EditTextEvent>()
    val event = eventChannel.asSharedFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            when(val user = editTextRepo.getUser(state.userId ?: "")) {
                is Result.Success -> {
                    state = state.copy(
                        userId = state.userId,
                        email = user.data.email,
                        firstName = user.data.firstName,
                        lastName = user.data.lastName,
                        phoneNumber = user.data.phoneNumber,
                        companyAddress = user.data.companyAddress,
                        companyName = user.data.companyName)

                }
                is Result.Error -> {

                }
            }
        }
    }

    fun onEvent(event: EditTextEvent) {
        when (event) {
            EditTextEvent.OnBackPress -> {} //handled in navigation
            EditTextEvent.OnSavePressed -> save()
            is EditTextEvent.OnTextChanged -> {
                state = state.copy(text = event.text) }
            EditTextEvent.SaveSuccessful -> {} //handled in navigation
            is EditTextEvent.InvalidEntry -> {} //handled in navigation
            EditTextEvent.ValidEntry -> {} //handled in navigation
        }
    }

    fun setTitleText(title: String, context: Context) {
        state = state.copy(title = determineTitleText(title, context), type = EditTextType.valueOf(title))
    }

    private fun determineTitleText(title: String, context: Context): String? {
        var titleString: String? = null
        when(EditTextType.valueOf(title)) {
            EditTextType.CHANGE_PHONE_NUMBER -> {
                titleString = context.getString(R.string.change_phone_number)
            }
            EditTextType.CHANGE_COMPANY_NAME -> {
                titleString = context.getString(R.string.change_company_name)
            }
            EditTextType.CHANGE_COMPANY_ADDRESS -> {
                titleString = context.getString(R.string.change_company_address)
            }
            EditTextType.CHANGE_EMAIL -> {
                titleString = context.getString(R.string.change_email)
            }
            EditTextType.CHANGE_NAME -> {
                titleString = context.getString(R.string.change_name)
            }
            EditTextType.CHANGE_PASSWORD -> {
                titleString = context.getString(R.string.change_password)
            }
        }
        return titleString
    }

    fun setText(text: String) {
        state = state.copy(text = text)
    }

    private fun save() {

        when(state.type) {
            EditTextType.CHANGE_PHONE_NUMBER -> {
                state = state.copy(phoneNumber = state.text)
                saveUser()
            }
            EditTextType.CHANGE_COMPANY_NAME -> {
                state = state.copy(companyName = state.text)
                saveUser()
            }
            EditTextType.CHANGE_COMPANY_ADDRESS -> {
                state = state.copy(companyAddress = state.text)
                saveUser()
            }
            EditTextType.CHANGE_EMAIL -> {
                viewModelScope.launch {
                    when (val result = validatorUseCase.isEmailValid(state.text ?: "")) {
                        is Result.Error -> {
                            eventChannel.emit(EditTextEvent.InvalidEntry(result.error.asUiText()))
                        }

                        is Result.Success -> {
                            state = state.copy(email = state.text)
                            saveUser()
                        }
                    }
                }
            }
            EditTextType.CHANGE_NAME -> {
                state = state.copy(name = state.text)
                splitFullName(state.text ?: "")
                saveUser()
            }
            EditTextType.CHANGE_PASSWORD -> {
                viewModelScope.launch {
                    when (val result = validatorUseCase.isPasswordValid(state.text ?: "")) {
                        is Result.Error -> {
                            eventChannel.emit(EditTextEvent.InvalidEntry(result.error.asUiText()))
                        }
                        is Result.Success -> {
                            eventChannel.emit(EditTextEvent.ValidEntry)
                            savePassword()
                        }
                    }
                }
            }
            else -> {}
        }

    }

    private fun savePassword() {
        viewModelScope.launch {
            when(val result = editTextRepo.updatePasswordToApi(state.email ?: "",state.text ?: "")) {
                is Result.Error -> {
                    eventChannel.emit(EditTextEvent.InvalidEntry(result.error.asUiText()))
                }
                is Result.Success -> {
                    eventChannel.emit(EditTextEvent.SaveSuccessful)
                }
            }
        }
    }

    private fun saveUser() {
        state = state.copy(isLoggingIn = true)

        val newUser = User(
            userId = state.userId ?: "",
            email = state.email ?: "",
            firstName = state.firstName ?: "",
            lastName = state.lastName ?: "",
            phoneNumber = state.phoneNumber ?: "",
            companyAddress = state.companyAddress ?: "",
            companyName = state.companyName ?: "",
        )

        viewModelScope.launch {
            when(editTextRepo.updateUserToDB(newUser)) {
                is Result.Success -> {
                    when(editTextRepo.updateUserToApi(newUser)) {
                        is Result.Success -> {
                            eventChannel.emit(EditTextEvent.SaveSuccessful)
                            state = state.copy(isLoggingIn = false)
                        }
                        is Result.Error -> {
                            state = state.copy(isLoggingIn = false)
                            TODO("handle network error")
                        }
                    }
                }
                is Result.Error -> {
                    state = state.copy(isLoggingIn = false)
                    TODO("handle local error")
                }
            }
        }
    }

    private fun splitFullName(fullName: String) {
        // Split the full name on the first space
        val parts = fullName.split(" ", limit = 2)

        // Assign first name and last name
        val firstName = parts[0]
        val lastName = if (parts.size > 1) parts[1] else ""

       state = state.copy(firstName = firstName, lastName = lastName)
    }
}

data class EditTextState(
    val title: String? = null,
    val type: EditTextType? = null,
    val isLoggingIn: Boolean = false,
    val text: String? = null,
    val userId: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val companyAddress: String? = null,
    val companyName: String? = null,
    val name: String? = null,
)