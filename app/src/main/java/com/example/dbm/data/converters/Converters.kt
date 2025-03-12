package com.example.dbm.data.converters

import android.net.Uri
import androidx.room.TypeConverter
import com.example.dbm.job.presentation.objects.Photo
import com.example.dbm.job.presentation.objects.Question
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val gson = GsonBuilder()
        .registerTypeAdapter(Uri::class.java, UriTypeAdapter())
        .create()

    @TypeConverter
    fun fromPhotoList(photoList: List<Photo>?): String? {
        return photoList?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toPhotoList(photoListString: String?): List<Photo>? {
        if (photoListString.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<Photo>>() {}.type
        return gson.fromJson(photoListString, type)
    }

    @TypeConverter
    fun fromQuestionList(value: List<Question>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toQuestionList(value: String): List<Question>? {
        val listType = object : TypeToken<List<Question>>() {}.type
        return gson.fromJson(value, listType)
    }

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