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

    override fun addUserFirstName(firstName: String) {
        sharedPref.edit().putString("first_name", firstName).apply()
    }

    override fun getUserFirstName(): String {
        return sharedPref.getString("first_name", "") ?: ""
    }

    override fun addUserLastName(lastName: String) {
        sharedPref.edit().putString("last_name", lastName).apply()
    }

    override fun getUserLastName(): String {
        return sharedPref.getString("last_name", "") ?: ""
    }

    override fun addUserEmail(email: String) {
        return sharedPref.edit().putString("email", email).apply()
    }

    override fun getUserEmail(): String {
        return sharedPref.getString("email", "") ?: ""
    }

    override fun addUserPhoneNumber(number: String) {
        return sharedPref.edit().putString("number", number).apply()
    }

    override fun getUserPhoneNumber(): String {
        return sharedPref.getString("number", "") ?: ""
    }

    override fun addUserId(id: String) {
        return sharedPref.edit().putString("userId", id).apply()
    }

    override fun getUserId(): String {
        val id: String = sharedPref.getString("userId", "") ?: ""
        return id
    }

    override fun addCompanyName(name: String) {
        return sharedPref.edit().putString("companyName", name).apply()
    }

    override fun getCompanyName(): String {
        return sharedPref.getString("companyName", "") ?: ""
    }

    override fun addCompanyAddress(address: String) {
        return sharedPref.edit().putString("companyAddress", address).apply()
    }

    override fun getCompanyAddress(): String {
        return sharedPref.getString("companyAddress", "") ?: ""
    }
}