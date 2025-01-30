package com.example.dbm.job_search.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.error_handling.domain.asUiText
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.job_search.domain.SearchScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    userPreferences: UserPreferences,
    private val searchScreenRepository: SearchScreenRepository
) : ViewModel() {

    var state by mutableStateOf(
        SearchState(
            email = userPreferences.getUserEmail(),
            name = userPreferences.getUserFullName(),
            userId = userPreferences.getUserId()
        )
    )
        private set

    private val eventChannel = MutableSharedFlow<SearchEvents>()
    val event = eventChannel.asSharedFlow()

    // Exposing Flow<List<Job>>
    val jobs: Flow<List<Job>> = flow {
        val userId = userPreferences.getUserId()
        when (val result = searchScreenRepository.getJobs(userId)) {
            is Result.Success -> {
                result.data.collect { jobList ->
                    emit(jobList) // Emit the jobs list
                }
            }
            is Result.Error -> {
                eventChannel.emit(SearchEvents.RetrievingJobsFailed(result.error.asUiText()))
                emit(emptyList()) // Emit empty list on error
            }
        }
    }

    fun onEvent(event: SearchEvents) {
        when(event) {
            is SearchEvents.OnSearchTextChange -> state = state.copy(searchText = event.searchText)
            is SearchEvents.OnBackPressed -> {} //handle in navigation
            is SearchEvents.OnLogoutPressed -> {} //handle in navigation
            is SearchEvents.OnUserSettingsPressed -> {} //handle in navigation
            is SearchEvents.OnContactUsPressed -> {} //handle in navigation
            is SearchEvents.OnAccountSettingsPressed -> {}//handle in navigation
        }
    }

    }

    data class SearchState(
        val email: String? = null,
        val name: String? = null,
        val userId: String? = null,
        val jobId: String? = null,
        val isSearching: Boolean = false,
        val isUserSettingsDropDownExpanded: Boolean = false,
        val jobList: MutableList<Job> = mutableStateListOf(),
        val selectedJobId: String? = null,
        val searchText: String? = null
    )