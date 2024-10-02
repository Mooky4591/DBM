package com.example.dbm.job.presentation.objects

import java.time.LocalDate

data class Job (
    var jobId: String?,
    var email: String?,
    var name: String?,
    var userId: String?,
    var phoneNumber: String?,
    var companyName: String?,
    var companyAddress: String?,
    var pictureUrl: String?,
    var dateCreated: LocalDate?,
    var questionList: List<Question>?
)