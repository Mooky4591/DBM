package com.example.dbm.data.remote.dtos


data class RegisterUserDTO(
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone_number: String,
    val password: String
)
