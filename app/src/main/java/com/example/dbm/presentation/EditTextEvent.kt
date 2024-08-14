package com.example.dbm.presentation

import com.example.dbm.navigation.Screen

sealed interface EditTextEvent {
    data object OnBackPress: EditTextEvent
    data object OnSavePressed: EditTextEvent
    data class OnTextChanged(val text: String): EditTextEvent
}