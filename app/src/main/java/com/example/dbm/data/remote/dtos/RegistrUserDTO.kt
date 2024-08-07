package com.example.dbm.data.remote.dtos


data class RegisterUserDTO(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val companyName: String,
    val companyAddress: String
)
