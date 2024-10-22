package com.example.dbm.job.domain

import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.presentation.objects.Job
import javax.inject.Inject

class SaveJobUseCase @Inject constructor(
    private val jobRepository: JobRepository
) {
    suspend fun saveJobToApi(job: Job): Result<Boolean, DataError.Network> {
        return jobRepository.saveJobToApi(job)
    }

    suspend fun saveJobToDB(job: Job): Result<Boolean, DataError.Local> {
        return jobRepository.saveJobToDB(job)
    }
}