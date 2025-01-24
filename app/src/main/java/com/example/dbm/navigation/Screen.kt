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
    data class JobsHistory(val userId: String)

    @Serializable
    data object AccountSettings

    @Serializable
    data object ContactUs

    @Serializable
    data class Job(val jobId: String)

    @Serializable
    data class EditText(val text: String?, val title: String)
}