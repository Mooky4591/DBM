package com.example.dbm.data.remote.dtos

data class JobDTO (
    val formId: String,
    val companyAddress: String,
    val companyName: String,
    val createdBy: String,
    val dateCreated: String,
    val questionsAndAnswers: Map<QuestionDTO, AnswerDTO>
)

data class QuestionDTO (
    val questionId: String,
    val questionText: String,
)

data class AnswerDTO (
    val answerText: String,
    val pictureUrl: String? = null
)