package com.example.dbm.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object Login

    @Serializable
    data object Register

    @Serializable
    data object Main

    @Serializable
    data object Search

    @Serializable
    data class FormsHistory(val userId: String)

    @Serializable
    data object AccountSettings

    @Serializable
    data class EditForm(val formId: String)

    @Serializable
    data object Job
}