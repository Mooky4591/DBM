package com.example.dbm.data.remote.dtos


data class JobDTO(
    val formId: String,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val companyAddress: String,
    val companyName: String,
    val createdBy: String,
    val dateCreated: String,
    val questionsAndAnswers: List<Map<String, Any?>>,
    val photoList: List<PhotoDto>,
    val wasSubmitted: Boolean
)