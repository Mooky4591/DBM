package com.example.dbm.main.domain

import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.presentation.objects.Job
import kotlinx.coroutines.flow.Flow

interface MainScreenRepository {
    suspend fun getUserName(email: String): String
    suspend fun clearDB(): Result<Boolean, DataError.Local>
    fun getUnsubmittedJobsFromDB(): Result<Flow<List<Job>>, DataError.Local>
    suspend fun deleteUnsubmittedJobsFromDB(jobId: String): Result<Boolean, DataError.Local>
    suspend fun deleteJob(jobId: String): Result<Boolean, DataError.Network>
}