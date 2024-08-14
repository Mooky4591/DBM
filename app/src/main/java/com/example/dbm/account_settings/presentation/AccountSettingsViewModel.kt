package com.example.dbm.account_settings.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dbm.domain.user_preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    var state by mutableStateOf(SettingState())
        private set

    private val eventChannel = Channel<AccountSettingEvent>()
    val event = eventChannel.receiveAsFlow()

    init {
        state = state.copy(name = userPreferences.getUserFullName())
    }

    fun onEvent(event: AccountSettingEvent) {

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
