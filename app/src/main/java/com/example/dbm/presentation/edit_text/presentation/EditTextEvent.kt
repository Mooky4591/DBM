package com.example.dbm.presentation.edit_text.presentation

import com.example.dbm.presentation.UiText

sealed interface EditTextEvent {
    data object OnBackPress: EditTextEvent
    data object OnSavePressed: EditTextEvent
    data class OnTextChanged(val text: String): EditTextEvent
    data class InvalidEntry(val errorMessage: UiText) : EditTextEvent
    data object ValidEntry : EditTextEvent
    data object SaveSuccessful: EditTextEvent
}