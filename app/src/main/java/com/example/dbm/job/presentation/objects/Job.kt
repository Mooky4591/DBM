package com.example.dbm.job.presentation.objects

import java.time.LocalDate

data class Job (
    var formId: String?,
    var email: String?,
    var name: String?,
    var userId: String?,
    var phoneNumber: String?,
    var companyName: String?,
    var companyAddress: String?,
    var dateCreated: LocalDate?,
    var questionsAndAnswers: List<Question>?,
    var photoList: List<Photo>?,
    var wasSubmitted: Boolean?
)