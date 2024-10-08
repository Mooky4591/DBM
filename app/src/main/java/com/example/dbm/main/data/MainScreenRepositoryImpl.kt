package com.example.dbm.main.data

import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.data.local.database.DbmDatabase
import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.LocalDataErrorHelper
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.main.domain.MainScreenRepository
import kotlinx.coroutines.runBlocking
import okio.IOException
import javax.inject.Inject

class MainScreenRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
): MainScreenRepository {

    override suspend fun getUserName(email: String): String {
        val name = userDao.getUserNameByEmail(email)
        return name
    }

    override suspend fun clearDB(): Result<Boolean, DataError.Local> {
        return try {
            //database.clearAllTables()
            TODO("Need to get this working")
            Result.Success(true)
        } catch (e: IOException) {
            LocalDataErrorHelper.determineLocalDataErrorMessage(e.message ?: "")
        } as Result<Boolean, DataError.Local>

    }
}