package com.example.dbm.presentation.edit_text.presentation

sealed interface EditTextEvent {
    data object OnBackPress: EditTextEvent
    data object OnSavePressed: EditTextEvent
    data class OnTextChanged(val text: String): EditTextEvent
    data object SaveSuccessful: EditTextEvent
}