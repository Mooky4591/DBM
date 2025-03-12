package com.example.dbm.job.presentation.objects

import android.net.Uri
import com.example.dbm.job.constants.QuestionIds
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Photo (
    val photoUrl: String?,
    @Transient val photo: Uri? = null,
    val questionIds: QuestionIds
)