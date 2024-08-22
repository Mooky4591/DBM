package com.example.dbm.main.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.main.domain.MainScreenRepository
import com.example.dbm.main.presentation.objects.Forms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainScreenRepo: MainScreenRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set
    private val eventChannel = Channel<MainEvents>()
    val event = eventChannel.receiveAsFlow()

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            state = state.copy(name = mainScreenRepo.getUserName(email = userPreferences.getUserEmail()))
            state = state.copy(email = userPreferences.getUserEmail())
        }

    }

    fun onEvent(event: MainEvents) {
        when (event) {
            is MainEvents.StartNewProject -> TODO()
            is MainEvents.UnsubmittedFormSelected -> TODO()
            is MainEvents.OnFormsHistorySelected -> TODO()
            is MainEvents.OnSearchSelected -> TODO()
            is MainEvents.OnUserSettingsSelected -> state =
                state.copy(isUserSettingsSelected = event.isUserDropDownSelected)

            is MainEvents.OnBackPress -> { /*handled in navigation*/
            }

            is MainEvents.OnAccountSettingsPressed -> { /*handled in navigation*/
            }

            is MainEvents.OnContactUsPressed -> { /*handled in navigation*/
            }

            is MainEvents.OnLogoutPressed -> {
                logout(event)
            }
        }
    }

    private fun logout(event: MainEvents) {
        userPreferences.clearPreference()
        viewModelScope.launch {
           // when (mainScreenRepo.clearDB()) {
            //    is Result.Success -> {
                    eventChannel.send(event)
                }
            //    is Result.Error -> {
              //      TODO("add error handling")
              //  }
           // }
       // }
    }
}

data class MainState(
    val name: String? = null,
    val email: String? = null,
    val formId : String? = null,
    val userId: String? = null,
    val searchParameters: String? = null,
    val isUserSettingsSelected: Boolean = false,
    val unsubmittedProjects: List<Forms> = listOf(),
    val date: LocalDate = LocalDate.now()
)