package com.example.dbm.job.presentation.objects

import com.example.dbm.job.constants.QuestionIds

data class Question (
    var questionText: String?,
    var questionId: QuestionIds?,
    var answer: String?
)