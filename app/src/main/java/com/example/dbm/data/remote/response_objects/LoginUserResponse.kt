package com.example.dbm.data.remote.response_objects

import com.example.dbm.job.presentation.objects.Job

data class LoginUserResponse(
    val userId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val companyAddress: String,
    val companyName: String,
    val jobs: List<Job>?
) {
}