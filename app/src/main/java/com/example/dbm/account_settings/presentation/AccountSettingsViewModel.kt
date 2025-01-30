package com.example.dbm.account_settings.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbm.account_settings.domain.AccountSettingsRepository
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.error_handling.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val accountSettingsRepo: AccountSettingsRepository
) : ViewModel() {
    var state by mutableStateOf(SettingState(
        name = userPreferences.getUserFullName(),
        userId = userPreferences.getUserId()
    ))
        private set

    private val eventChannel = MutableSharedFlow<AccountSettingEvent>()
    val event = eventChannel.asSharedFlow()


    init {
        loadUserSettings()
    }

    fun onEvent(event: AccountSettingEvent) {

    }

    private fun loadUserSettings(){
        val id: String = state.userId ?: ""
        viewModelScope.launch {
            when(val user = accountSettingsRepo.getUserAccountInfo(userId = id)) {
                is Result.Success -> {
                    state = state.copy(
                        userId = id,
                        email = user.data.email,
                        phoneNumber = user.data.phoneNumber,
                        companyName = user.data.companyName,
                        companyAddress = user.data.companyAddress,
                        name = user.data.firstName + " " + user.data.lastName
                    )
                }
                is Result.Error -> {

                }
            }
        }

    }
}

data class SettingState(
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val companyName: String? = null,
    val companyAddress: String? = null
)
