package com.example.dbm.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.dbm.data.local.entities.JobEntity

@Dao
interface JobDao {
    @Upsert
    suspend fun insertJob(jobEntity: JobEntity)

    @Query("SELECT * FROM jobs_table WHERE wasSubmitted = 0")
    suspend fun getUnfinishedJobs(): List<JobEntity>
}