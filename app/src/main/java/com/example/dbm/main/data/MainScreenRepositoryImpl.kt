package com.example.dbm.main.data

import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.main.domain.MainScreenRepository
import javax.inject.Inject

class MainScreenRepositoryImpl @Inject constructor(
    private val userDao: UserDao
): MainScreenRepository {

    override suspend fun getUserName(email: String): String {
        val name = userDao.getUserNameByEmail(email)
        return name
    }
}