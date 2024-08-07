package com.example.dbm.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    var id: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String,
    var companyName: String,
    var companyAddress: String
)
