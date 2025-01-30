package com.example.dbm.job_search.domain

import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.presentation.objects.Job
import kotlinx.coroutines.flow.Flow


interface SearchScreenRepository {
    fun getJobs(userId: String): Result<Flow<List<Job>>, DataError.Local>
}