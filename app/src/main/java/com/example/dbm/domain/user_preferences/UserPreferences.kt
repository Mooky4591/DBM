package com.example.dbm.domain.user_preferences

import com.example.dbm.login.presentation.objects.User

interface UserPreferences {
    fun clearPreference()
    fun addUserFullName(fullName: String)
    fun getUserFullName(): String
    fun addUserFirstName(firstName: String)
    fun getUserFirstName(): String
    fun addUserLastName(lastName: String)
    fun getUserLastName(): String
    fun addUserEmail(email: String)
    fun getUserEmail(): String
    fun addUserId(id: String)
    fun getUserId(): String
}