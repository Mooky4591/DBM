package com.example.dbm.job.presentation

import android.net.Uri
import com.example.dbm.job.constants.QuestionIds
import com.example.dbm.presentation.UiText


interface JobEvents {
    data object OnBackPress : JobEvents
    data class OnCheckMarkSelected(val response: String, val questionIds: QuestionIds, val questionTxt: String, val isChecked: Boolean, val index: Int) : JobEvents
    data class OnQuestionAnswered(val response: String, val questionId: QuestionIds, val questionTxt: String) : JobEvents
    data class OnPhotoAdded(val uri: Uri, val questionId: QuestionIds, val questionTxt: String) : JobEvents
    data object OnSaveJob: JobEvents
    data object OnSaveSuccessful : JobEvents
    data class OnSaveFailed(val failureMessage: UiText) : JobEvents
    data class RemovePhoto(val uri: Uri, val questionId: QuestionIds) : JobEvents
    data class ToggleRemovePhotoMenu(val toggleRemovePhotoMenu: Boolean) : JobEvents
}