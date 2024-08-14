package com.example.dbm.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs_table")
data class JobEntity(
    @PrimaryKey
    var formId: String,
    var questionId: String,
    var questionText: String,
    var responseText: String,
    var pictureUrl: String,
    var companyName: String,
    var companyAddress: String,
    var dateCreated: String,
    var createdBy: String
)
