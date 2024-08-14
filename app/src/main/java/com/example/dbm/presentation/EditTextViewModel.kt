package com.example.dbm.presentation

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
class EditTextViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    var state by mutableStateOf(EditTextState())
        private set
    private val eventChannel = Channel<EditTextEvent>()
    val event = eventChannel.receiveAsFlow()

    init {
        state = state.copy(name = userPreferences.getUserFullName())
    }

    fun onEvent(event: EditTextEvent) {
        when (event) {
            EditTextEvent.OnBackPress -> {} //handled in navigation
            EditTextEvent.OnSavePressed -> save()
            is EditTextEvent.OnTextChanged -> {
                state = state.copy(text = event.text) }
        }
    }

    fun setTitleText(title: String) {
        state = state.copy(title = title)
    }

    fun setText(text: String) {
        state = state.copy(text = text)
    }

    private fun save() {
        TODO("save the changed value to the DB")
    }
}

data class EditTextState(
    val title: String? = null,
    val text: String? = null,
    val name: String? = null
)