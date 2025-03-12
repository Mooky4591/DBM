package com.example.dbm.job.domain.objects

import androidx.room.TypeConverters
import com.example.dbm.data.converters.Converters
import com.example.dbm.job.presentation.objects.Photo
import com.example.dbm.job.presentation.objects.Question
import kotlinx.serialization.Serializable

@TypeConverters(Converters::class)
@Serializable
data class JobData(
    val questionList: List<Question>?,
    val photoList: List<Photo>?
)
