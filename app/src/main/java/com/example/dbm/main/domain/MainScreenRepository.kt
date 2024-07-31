package com.example.dbm.main.domain

interface MainScreenRepository {
    suspend fun getUserName(email: String): String
}