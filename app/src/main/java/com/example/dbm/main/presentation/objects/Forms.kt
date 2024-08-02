package com.example.dbm.main.presentation.objects

import java.time.LocalDate

data class Forms (
    val formId: String,
    val dateCreated: LocalDate,
    val createdBy: String,
    val jobAddress: String
)
