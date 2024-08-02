package com.example.dbm.main.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.main.domain.MainScreenRepository
import com.example.dbm.main.presentation.objects.Forms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
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
        setUserInitials()
    }

    fun onEvent(event: MainEvents) {
        when (event) {
            is MainEvents.StartNewProject -> TODO()
            is MainEvents.UnsubmittedFormSelected -> TODO()
            is MainEvents.OnFormsHistorySelected -> TODO()
            is MainEvents.OnSearchSelected -> TODO()
            is MainEvents.OnUserSettingsSelected -> TODO()
        }
    }
    private fun setUserInitials() {
        val email: String = userPreferences.getUserEmail()
        viewModelScope.launch {
            val name: String = mainScreenRepo.getUserName(email)
            state = state.copy(name = name)
            state = state.copy(initials = name
                .split(' ')
                .mapNotNull { it.firstOrNull()?.toString() }
                .reduce { acc, s -> acc + s })
        }
    }
}

data class MainState(
    val name: String? = null,
    val email: String? = null,
    val initials: String? = null,
    val formId : String? = null,
    val userId: String? = null,
    val searchParameters: String? = null,
    val isUserSettingsSelected: Boolean = false,
    val unsubmittedProjects: List<Forms> = listOf(),
    val date: LocalDate = LocalDate.now()
)