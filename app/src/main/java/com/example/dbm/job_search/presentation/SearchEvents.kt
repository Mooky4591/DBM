package com.example.dbm.job_search.presentation

import com.example.dbm.presentation.UiText

interface SearchEvents {
    data object OnBackPressed: SearchEvents
    data object OnUserSettingsPressed: SearchEvents
    data object OnAccountSettingsPressed : SearchEvents
    data object OnLogoutPressed : SearchEvents
    data object OnContactUsPressed : SearchEvents
    data class OnJobSelected(val formId: String) : SearchEvents
    data class  RetrievingJobsFailed(val errorText: UiText) : SearchEvents
    data class OnSearchTextChange(val searchText: String) : SearchEvents
}