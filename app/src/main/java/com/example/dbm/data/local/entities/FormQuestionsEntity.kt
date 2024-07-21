package com.example.dbm.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.text.Normalizer.Form

@Entity(tableName = "form_questions_table")
data class FormQuestionsEntity(
    @PrimaryKey
    var questionId: String,
    var formId: String,
    var question: String
)
