package com.example.dbm.data.remote.response_objects

data class LoginUserResponse(
    val userId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val companyAddress: String,
    val companyName: String
) {
}