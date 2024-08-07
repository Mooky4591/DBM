package com.example.dbm.job.presentation


interface JobEvents {
    data object OnBackPress: JobEvents
    data class OnUserSettingsSelected(val isUserDropDownSelected: Boolean) : JobEvents

}