package com.example.dbm.main.presentation

sealed interface MainEvents {
    data class OnUserSettingsSelected(val isUserDropDownSelected: Boolean) : MainEvents
    data object OnSearchSelected : MainEvents
    data object OnBackPress: MainEvents
    data class OnFormsHistorySelected(val userId: String) : MainEvents
    data class UnsubmittedFormSelected(val formId: String) : MainEvents
    data object StartNewProject : MainEvents
}