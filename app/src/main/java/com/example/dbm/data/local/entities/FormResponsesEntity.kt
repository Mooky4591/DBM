package com.example.dbm.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "form_responses_table")
data class FormResponsesEntity(
    @PrimaryKey
    var questionId: String,
    var formId: String,
    var pictureUrl: String?,
    var response: String?
)
