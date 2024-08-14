package com.example.dbm.job.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dbm.domain.user_preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    userPreferences: UserPreferences
) : ViewModel() {
    var state by mutableStateOf(JobState())
    private set

    private val eventChannel = Channel<JobEvents>()
    val events = eventChannel.receiveAsFlow()

    init {
        state = state.copy(
            email = userPreferences.getUserEmail(),
            name = userPreferences.getUserFullName(),
            createdBy = userPreferences.getUserId()
        )
    }

    fun onEvent(event: JobEvents) {
        when (event) {
            is JobEvents.OnUserSettingsSelected -> {
                state = state.copy(isUserSettingsSelected = event.userSettingsSelected)
            }
        }
    }

}

data class JobState(
    val email: String? = null,
    val name: String? = null,
    val userId: String? = null,
    val phoneNumber: String? = null,
    val companyName: String? = null,
    val companyAddress: String? = null,
    val jobId: String? = null,
    val questionId: String? = null,
    val questionTxt: String? = null,
    val response: String? = null,
    val pictureUrl: String? = null,
    val dateCreated: LocalDate = LocalDate.now(),
    val createdBy: String? = null,
    val isUserSettingsSelected: Boolean = false
)