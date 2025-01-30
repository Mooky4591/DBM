package com.example.dbm.main.presentation

import com.example.dbm.presentation.UiText


sealed interface MainEvents {
    data class OnUserSettingsSelected(val isUserDropDownSelected: Boolean) : MainEvents
    data object OnSearchSelected : MainEvents
    data object OnBackPress: MainEvents
    data class OnFormsHistorySelected(val userId: String) : MainEvents
    data object OnAccountSettingsPressed : MainEvents
    data object OnContactUsPressed : MainEvents
    data object OnLogoutPressed : MainEvents
    data object StartNewProject : MainEvents
    data class DeleteUnfinishedJob(val jobId: String) : MainEvents
    data object DeleteUnfinishedJobFailed : MainEvents
    data class OnUnfinishedJobSelected(val formId: String) : MainEvents
    data class ElipsisSelected(val selectedJobId: String?) : MainEvents
    data class RetrievingUnsubmittedJobsFailed(val errorText: UiText) : MainEvents
}