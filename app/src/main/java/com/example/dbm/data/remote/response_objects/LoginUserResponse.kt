package com.example.dbm.data.remote.response_objects

data class LoginUserResponse(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String
) {
}