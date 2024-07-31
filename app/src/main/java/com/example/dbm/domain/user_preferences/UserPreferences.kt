package com.example.dbm.domain.user_preferences

interface UserPreferences {
    fun clearPreference()
    fun addUserFullName(fullName: String)
    fun getUserFullName(): String
    fun addUserEmail(email: String)
    fun getUserEmail(): String
}