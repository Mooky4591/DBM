package com.example.dbm.job.domain

import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.domain.objects.JobData
import com.example.dbm.job.presentation.objects.Job
import kotlinx.coroutines.flow.Flow

interface JobRepository {
    suspend fun saveJob(job: Job): Result<Boolean, DataError.Network>
    suspend fun getJobDataByJobId(jobId: String): Result<JobData, DataError.Local>
    suspend fun getJobByJobId(jobId: String): Result<Boolean, DataError.Local>
    fun getUnsubmittedJobsFromDB(): Result<Flow<List<Job>>, DataError.Local>
    fun getJobsFromDB(userId: String): Result<Flow<List<Job>>, DataError.Local>
    suspend fun deleteUnsubmittedJobsFromDB(jobId: String): Result<Boolean, DataError.Local>
    suspend fun deleteUnsubmittedJobsFromAPI(jobId: String): Result<Boolean, DataError.Network>
}