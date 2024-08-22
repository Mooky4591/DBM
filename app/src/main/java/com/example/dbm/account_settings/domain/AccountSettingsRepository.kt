package com.example.dbm.account_settings.domain

import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.login.presentation.objects.User

interface AccountSettingsRepository {
    suspend fun getUserAccountInfo(userId: String): Result<User, DataError.Local>
}