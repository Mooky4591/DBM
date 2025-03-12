package com.example.dbm.main.data

import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.LocalDataErrorHelper
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.domain.JobRepository
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.main.domain.MainScreenRepository
import kotlinx.coroutines.flow.Flow
import okio.IOException
import javax.inject.Inject

class MainScreenRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val jobRepository: JobRepository
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

    override fun getUnsubmittedJobsFromDB(): Result<Flow<List<Job>>, DataError.Local> {
        return jobRepository.getUnsubmittedJobsFromDB()
    }

    override suspend fun deleteUnsubmittedJobsFromDB(jobId: String): Result<Boolean, DataError.Local> {
        return jobRepository.deleteUnsubmittedJobsFromDB(jobId)
    }

    override suspend fun deleteJob(jobId: String): Result<Boolean, DataError.Network> {
        return jobRepository.deleteUnsubmittedJobsFromAPI(jobId)
    }
}
