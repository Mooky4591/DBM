package com.example.dbm.data.remote.dtos

import java.util.UUID

data class RegisterUserDTO(
    val firstName: String,
    val lastName: String,
    val userId: UUID,
    val email: String,
    val phoneNumber: String,
    val password: String
)
