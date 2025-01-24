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
    suspend fun getJobByFormId(id: String): JobData
}