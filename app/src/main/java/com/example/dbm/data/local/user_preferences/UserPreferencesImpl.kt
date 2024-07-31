package com.example.dbm.data.local.user_preferences

import android.content.SharedPreferences
import com.example.dbm.domain.user_preferences.UserPreferences

class UserPreferencesImpl(
    private val sharedPref: SharedPreferences
): UserPreferences {
    override fun clearPreference() {
        sharedPref.edit().clear().apply()
    }

    override fun addUserFullName(fullName: String) {
        sharedPref.edit().putString("name", fullName).apply()
    }

    override fun getUserFullName(): String {
        return sharedPref.getString("name", "") ?: ""
    }

    override fun addUserEmail(email: String) {
        return sharedPref.edit().putString("email", email).apply()
    }

    override fun getUserEmail(): String {
        return sharedPref.getString("email", "") ?: ""
    }
}