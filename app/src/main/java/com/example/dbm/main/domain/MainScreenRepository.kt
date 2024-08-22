package com.example.dbm.main.domain

import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result

interface MainScreenRepository {
    suspend fun getUserName(email: String): String
    suspend fun clearDB(): Result<Boolean, DataError.Local>
}