package com.example.dbm.login.presentation.objects


data class User(
    val userId: String?,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val companyName: String,
    val companyAddress: String
) {
}
