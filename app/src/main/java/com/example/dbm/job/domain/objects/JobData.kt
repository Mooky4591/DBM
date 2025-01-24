package com.example.dbm.job.domain.objects

import com.example.dbm.job.presentation.objects.Photo
import com.example.dbm.job.presentation.objects.Question

data class JobData(
    val questionList: List<Question>?,
    val photoList: List<Photo>?
)
