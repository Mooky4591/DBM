package com.example.dbm.data.remote.dtos

data class JobDTO (
    val formId: String,
    val companyAddress: String,
    val companyName: String,
    val createdBy: String,
    val dateCreated: String,
    val questionsAndAnswers: List<QuestionAnswer>
)

data class QuestionAnswer (
    val questionId: String,
    val questionText: String,
    val answerText: String
)