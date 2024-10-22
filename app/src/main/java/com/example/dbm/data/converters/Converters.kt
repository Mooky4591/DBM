package com.example.dbm.data.converters

import android.net.Uri
import androidx.room.TypeConverter
import com.example.dbm.job.presentation.objects.Photo
import com.example.dbm.job.presentation.objects.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson<List<String>>(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromQuestionList(value: List<Question>?): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toQuestionList(value: String): List<Question>? {
        val gson = Gson()
        val listType = object : TypeToken<List<Question>>() {}.type
        return gson.fromJson(value, listType)
    }

    private val gson = Gson()

    // Convert Uri to String
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    // Convert String to Uri
    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }

    // Convert List<Photo> to JSON String
    @TypeConverter
    fun fromPhotoList(photoList: List<Photo>?): String? {
        return if (photoList == null) {
            null
        } else {
            gson.toJson(photoList)
        }
    }

    // Convert JSON String to List<Photo>
    @TypeConverter
    fun toPhotoList(photoListString: String?): List<Photo>? {
        return if (photoListString == null) {
            null
        } else {
            val type = object : TypeToken<List<Photo>>() {}.type
            gson.fromJson(photoListString, type)
        }
    }

    @TypeConverter
    fun fromDateString(value: String?): LocalDate? {
        return value?.let {
            LocalDate.parse(it, formatter)
        }
    }

    @TypeConverter
    fun dateToString(date: LocalDate?): String? {
        return date?.format(formatter)
    }
}