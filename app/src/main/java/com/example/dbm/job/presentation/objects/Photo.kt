package com.example.dbm.job.presentation.objects

import android.net.Uri
import com.example.dbm.job.constants.QuestionIds
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Photo (
    @Contextual var photo: Uri,
    val questionIds: QuestionIds
)