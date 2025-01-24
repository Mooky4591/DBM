package com.example.dbm.job.domain

import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.domain.objects.JobData
import com.example.dbm.job.presentation.objects.Job

interface JobRepository {
    suspend fun saveJobToApi(job: Job): Result<Boolean, DataError.Network>
    suspend fun saveJobToDB(job: Job): Result<Boolean, DataError.Local>
    suspend fun getJobByJobId(jobId: String): Result<JobData, DataError.Local>
}