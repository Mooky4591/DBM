package com.example.dbm.job.presentation

import com.example.dbm.job.constants.QuestionIds


interface JobEvents {
    data object OnBackPress : JobEvents
    data class OnCheckMarkSelected(val response: String, val questionIds: QuestionIds, val questionTxt: String, val isChecked: Boolean, val index: Int) : JobEvents
    data class OnQuestionAnswered(val response: String, val questionId: QuestionIds, val questionTxt: String) : JobEvents
}