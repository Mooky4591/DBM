package com.example.dbm.job_search.data

import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.domain.JobRepository
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.job_search.domain.SearchScreenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchScreenRepositoryImpl @Inject constructor(
    private val jobRepository: JobRepository
): SearchScreenRepository {
    override fun getJobs(userId: String): Result<Flow<List<Job>>, DataError.Local> {
        return jobRepository.getJobsFromDB(userId)
    }
}