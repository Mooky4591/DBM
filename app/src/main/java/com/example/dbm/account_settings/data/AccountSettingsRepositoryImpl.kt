package com.example.dbm.account_settings.data

import com.example.dbm.account_settings.domain.AccountSettingsRepository
import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.login.presentation.objects.User
import okio.IOException
import javax.inject.Inject

class AccountSettingsRepositoryImpl @Inject constructor(
    private val userDao: UserDao
): AccountSettingsRepository {

    override suspend fun getUserAccountInfo(userId: String): Result<User, DataError.Local> {
        return try {
            val user = userDao.getUserByUserId(userId)
            Result.Success(user)
        }
        catch (e: IOException) {
            when (e.message) {
                "Permission denied" -> Result.Error(DataError.Local.PERMISSION_DENIED)
                "File not found" -> Result.Error(DataError.Local.FILE_NOT_FOUND)
                "Disk full" -> Result.Error(DataError.Local.DISK_FULL)
                "Input/output error" -> Result.Error(DataError.Local.INPUT_OUTPUT_ERROR)
                "Connection refused" -> Result.Error(DataError.Local.CONNECTION_REFUSED)
                else -> Result.Error(DataError.Local.UNKNOWN)
            }
        }
    }
}