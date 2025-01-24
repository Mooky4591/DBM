package com.example.dbm.main.data

import com.example.dbm.data.local.daos.JobDao
import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.data.local.entities.JobEntity
import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.LocalDataErrorHelper
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.main.domain.MainScreenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

class MainScreenRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val jobDao: JobDao
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

    override suspend fun getUnsubmittedProjects(): Result<Flow<List<Job>>, DataError.Local> {
        return try {
            val job: Flow<List<Job>> = jobDao.getUnfinishedJobs().map { jobList ->
                jobList.map { it.toJob() }
            }
            Result.Success(job)
        } catch (e: IOException) {
            LocalDataErrorHelper.determineLocalDataErrorMessage(e.message ?: "")
        } as Result<Flow<List<Job>>, DataError.Local>
    }
}

private fun JobEntity.toJob(): Job {
    return Job(
        formId = formId,
        email = email,
        name = "",
        userId = userId,
        phoneNumber = phoneNumber,
        companyName = companyName,
        companyAddress = companyAddress,
        dateCreated = dateCreated,
        questionsAndAnswers = questionList,
        photoList = photoList,
        wasSubmitted = wasSubmitted
    )
}
