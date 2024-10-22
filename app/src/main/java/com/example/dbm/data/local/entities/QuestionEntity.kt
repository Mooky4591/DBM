package com.example.dbm.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions_table")
data class QuestionEntity(
    @PrimaryKey
    var formId: String,
    var questionId: String,
    var questionText: String,
    var responseText: String,
    var pictureUrl: String
)
