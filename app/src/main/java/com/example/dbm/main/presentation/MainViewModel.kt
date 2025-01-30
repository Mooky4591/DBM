package com.example.dbm.main.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.error_handling.domain.asUiText
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.main.domain.MainScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainScreenRepo: MainScreenRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    var state by mutableStateOf(MainState(
        name = userPreferences.getUserFullName(),
        email = userPreferences.getUserEmail(),
        userId = userPreferences.getUserId()
    )
    )
        private set
    private val eventChannel = MutableSharedFlow<MainEvents>()
    val event = eventChannel.asSharedFlow()

    val jobs: Flow<List<Job>> = flow {
        when (val result = mainScreenRepo.getUnsubmittedJobsFromDB()) {
            is Result.Success -> {
                result.data.collect { jobList ->
                    emit(jobList)
                }            }
            is Result.Error -> {
                eventChannel.emit(MainEvents.RetrievingUnsubmittedJobsFailed(result.error.asUiText()))
                    emit(emptyList())
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

            is MainEvents.ElipsisSelected -> {
                state = state.copy(selectedJobId = event.selectedJobId)
            }

            is MainEvents.DeleteUnfinishedJob -> {
                deleteUnfinishedJob(event.jobId)
            }

            else -> {}
        }
    }

    private fun deleteUnfinishedJob(jobId: String) {
        viewModelScope.launch {
            when(mainScreenRepo.deleteUnsubmittedJobsFromDB(jobId)) {
                is Result.Success -> {
                    mainScreenRepo.deleteJob(jobId)
                }
                is Result.Error -> {
                    eventChannel.emit(MainEvents.DeleteUnfinishedJobFailed)
                }
            }
        }
    }

    private fun logout(event: MainEvents) {
        userPreferences.clearPreference()
        viewModelScope.launch {
           // when (mainScreenRepo.clearDB()) {
            //    is Result.Success -> {
                    eventChannel.emit(event)
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
    val date: LocalDate = LocalDate.now(),
    val selectedJobId: String? = null
)