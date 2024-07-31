package com.example.dbm.main.presentation

sealed interface MainEvents {
    data class OnUserSettingsSelected(val userId: String) : MainEvents
    data object OnSearchSelected : MainEvents
    data class OnFormsHistorySelected(val userId: String) : MainEvents
    data class UnsubmittedFormSelected(val formId: String) : MainEvents
    data object StartNewProject : MainEvents
}