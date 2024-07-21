package com.example.dbm.login.domain.objects

import java.util.UUID

data class User(
    val userId: UUID,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val password: String?
)
