package com.example.dbm.presentation.edit_text.enum

import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.Serializable

enum class EditTextType {
    CHANGE_PHONE_NUMBER,
    CHANGE_COMPANY_NAME,
    CHANGE_COMPANY_ADDRESS,
    CHANGE_EMAIL,
    CHANGE_NAME,
    CHANGE_PASSWORD
}