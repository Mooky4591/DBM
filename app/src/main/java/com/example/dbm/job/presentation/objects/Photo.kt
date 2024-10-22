package com.example.dbm.job.presentation.objects

import android.net.Uri
import com.example.dbm.job.constants.QuestionIds

data class Photo (
    var photo: Uri,
    val questionIds: QuestionIds
)