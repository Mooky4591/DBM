package com.example.dbm.job.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(JobState())
    private set

    private val eventChannel = Channel<JobEvents>()
    val events = eventChannel.receiveAsFlow()

    fun onEvent(event: JobEvents) {

    }

}

data class JobState(
    val email: String? = null,
    val name: String? = null,
    val phoneNumber: String? = null,
    val companyName: String? = null,
    val companyAddress: String? = null,
    val isUserSettingsSelected: Boolean = false
)