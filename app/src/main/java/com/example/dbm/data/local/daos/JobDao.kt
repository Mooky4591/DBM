package com.example.dbm.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.dbm.data.local.entities.JobEntity
import com.example.dbm.job.domain.objects.JobData
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Upsert
    suspend fun insertJob(jobEntity: JobEntity)

    @Query("SELECT * FROM jobs_table WHERE wasSubmitted = 0")
    fun getUnfinishedJobs(): Flow<List<JobEntity>>

    @Query("SELECT questionList, photoList FROM jobs_table WHERE formId = :id")
    suspend fun getJobDataByFormId(id: String): JobData

    @Query("SELECT wasSubmitted FROM jobs_table WHERE formId = :id")
    suspend fun getJobByFormId(id: String): Int

    @Query("DELETE FROM jobs_table WHERE formId = :id")
    suspend fun deleteJob(id: String)

    @Query("SELECT * FROM jobs_table WHERE wasSubmitted = 1")
    fun getSubmittedJobs(): Flow<List<JobEntity>>
}