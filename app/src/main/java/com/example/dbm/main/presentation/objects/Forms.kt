package com.example.dbm.main.presentation.objects

import java.time.LocalDate

data class Forms (
    val formId: String,
    val dateCreated: LocalDate,
    val createdBy: String,
    val jobAddress: String,
    val companyName: String,
    val questionsAndAnswers: Map<Question, Answer>,
    val submitted: Boolean
)

data class Question (
    val questionId: String,
    val questionText: String,
)

data class Answer (
    val answerText: String,
    val pictureUrl: String? = null
)