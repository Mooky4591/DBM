package com.example.dbm.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forms_table")
data class FormsEntity(
    @PrimaryKey
    var formId: String,
    var dateCreated: String,
    var createdBy: String
)
