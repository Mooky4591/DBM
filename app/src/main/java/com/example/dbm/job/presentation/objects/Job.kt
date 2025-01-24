package com.example.dbm.job.presentation.objects

import LocalDateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Job (
    var formId: String?,
    var email: String?,
    var name: String?,
    var userId: String?,
    var phoneNumber: String?,
    var companyName: String?,
    var companyAddress: String?,
    @Serializable(with = LocalDateSerializer::class) var dateCreated: LocalDate?,
    @Contextual var questionsAndAnswers: List<Question>?,
    @Contextual var photoList: List<Photo>?,
    var wasSubmitted: Boolean?
)