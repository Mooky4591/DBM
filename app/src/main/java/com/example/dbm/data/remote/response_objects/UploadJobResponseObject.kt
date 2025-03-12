package com.example.dbm.data.remote.response_objects

import com.example.dbm.job.constants.QuestionIds

data class UploadJobResponseObject(
    val photoUrl: String?,
    val questionId: QuestionIds?
)
