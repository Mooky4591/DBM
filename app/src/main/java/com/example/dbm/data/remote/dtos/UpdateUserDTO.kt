package com.example.dbm.data.remote.dtos

data class UpdateUserDTO(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val companyName: String,
    val companyAddress: String
)
