package com.example.dbm.presentation.edit_text.domain

import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.login.presentation.objects.User

interface EditTextRepository {
    suspend fun updateUserToDB(user: User): Result<User, DataError.Local>
    suspend fun updateUserToApi(user: User): Result<Boolean, DataError.Network>
    suspend fun getUser(userId: String): Result<User, DataError.Local>
}