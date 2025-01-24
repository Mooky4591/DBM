package com.example.dbm.main.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.main.domain.MainScreenRepository
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


                when(val project = mainScreenRepo.getUnsubmittedProjects()) {
                    is Result.Error -> TODO()
                    is Result.Success -> {
                        project.data.collect { value ->
                            state = state.copy(unsubmittedProjects = value.toMutableStateList())
                        }
                    }
                }
        }

    }

    fun onEvent(event: MainEvents) {
        when (event) {
            is MainEvents.StartNewProject -> { /*handled in navigation*/}

            is MainEvents.OnFormsHistorySelected -> TODO()
            is MainEvents.OnSearchSelected -> TODO()
            is MainEvents.OnUnfinishedJobSelected -> { /*handled in navigation*/
            }
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
    val unsubmittedProjects: MutableList<Job> = mutableStateListOf(),
    val date: LocalDate = LocalDate.now()
)