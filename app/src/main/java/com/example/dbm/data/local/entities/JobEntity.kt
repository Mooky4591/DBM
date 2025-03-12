package com.example.dbm.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.dbm.data.converters.Converters
import com.example.dbm.job.presentation.objects.Photo
import com.example.dbm.job.presentation.objects.Question
import java.time.LocalDate

@TypeConverters(Converters::class)
@Entity(tableName = "jobs_table")
data class JobEntity(
    @PrimaryKey
    var formId: String,
    var companyName: String,
    var companyAddress: String,
    var dateCreated: LocalDate,
    var userId: String,
    var phoneNumber: String,
    var email: String,
    var wasSubmitted: Boolean,
    var questionList: List<Question>?,
    var photoList: List<Photo>?
)
