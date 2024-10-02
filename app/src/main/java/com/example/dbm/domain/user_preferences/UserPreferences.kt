package com.example.dbm.domain.user_preferences

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
    fun addUserPhoneNumber(number: String)
    fun getUserPhoneNumber(): String
    fun addUserId(id: String)
    fun getUserId(): String
    fun addCompanyName(name: String)
    fun getCompanyName(): String
    fun addCompanyAddress(address: String)
    fun getCompanyAddress(): String
}
