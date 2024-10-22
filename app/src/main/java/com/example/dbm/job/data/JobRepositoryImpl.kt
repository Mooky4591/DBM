package com.example.dbm.job.data

import android.content.Context
import android.net.Uri
import com.example.dbm.data.local.daos.JobDao
import com.example.dbm.data.local.entities.JobEntity
import com.example.dbm.data.remote.DBMApi
import com.example.dbm.data.remote.dtos.JobDTO
import com.example.dbm.data.remote.dtos.PhotoDto
import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.LocalDataErrorHelper
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.domain.JobRepository
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.job.presentation.objects.Question
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject



class JobRepositoryImpl @Inject constructor(
    private val dbmAPi: DBMApi,
    private val jobDao: JobDao,
    private val context: Context
): JobRepository {

    override suspend fun saveJobToApi(job: Job): Result<Boolean, DataError.Network> {
        return try {
            val jobDTO = job.toJobDto(context)
            dbmAPi.uploadNewJob(jobDTO)
            Result.Success(true)
        } catch (e: HttpException) {
            LocalDataErrorHelper.determineNetworkDataErrorMessage(e.code())
        } as Result<Boolean, DataError.Network>
    }

    override suspend fun saveJobToDB(job: Job): Result<Boolean, DataError.Local> {
        return try {
            jobDao.insertJob(job.toJobEntity())
            Result.Success(true)
        } catch (e: IOException) {
            LocalDataErrorHelper.determineLocalDataErrorMessage(e.message ?: "")
        } as Result<Boolean, DataError.Local>
    }
}

private fun Job.toJobEntity(): JobEntity {
    return JobEntity(
        formId = formId ?: "",
        companyName = companyName ?: "",
        companyAddress = companyAddress ?: "",
        userId = userId ?: "",
        dateCreated = dateCreated!!,
        email = email ?: "",
        phoneNumber = phoneNumber ?: "",
        wasSubmitted = wasSubmitted ?: false,
        questionList = questionsAndAnswers!!,
        photoList = photoList ?: emptyList()
    )
}

private fun Job.toJobDto(context: Context): JobDTO {
    val byteArrayList = mutableListOf<PhotoDto>()
    photoList?.takeIf { it.isNotEmpty() }?.forEach { photo ->
        val photoDto = getImageDataFromUri(photo.photo, context)?.let {
            PhotoDto(
                photo = it,
                questionIds = photo.questionIds
            )
        }
        if (photoDto != null) {
            byteArrayList.add(photoDto)
        }
    }
    val questionDic = questionsAndAnswers.toDictionary()

 return JobDTO(
     formId = formId!!,
     companyAddress = companyAddress ?: "" ,
     companyName = companyName ?: "",
     createdBy = userId ?: "",
     dateCreated = dateCreated.toString(),
     questionsAndAnswers = questionDic,
     photoList = byteArrayList,
     email = email!!,
     name = name ?: "",
     phoneNumber = phoneNumber ?: "",
     wasSubmitted = wasSubmitted ?: false
 )
}

private fun List<Question>?.toDictionary(): List<Map<String, Any?>>{
    return this?.map { question ->
        mapOf(
            "questionText" to question.questionText,
            "questionId" to question.questionId?.toString(),  // Convert to string if needed
            "answer" to question.answer,
        )
    } ?: emptyList()
}

fun getImageDataFromUri(photo: Uri, context: Context): ByteArray? {
    return try {
        // Open an InputStream for the given URI
        val inputStream: InputStream? = context.contentResolver.openInputStream(photo)
        // Read the input stream and convert it to a ByteArray
        inputStream?.use {
            val buffer = ByteArrayOutputStream()
            val data = ByteArray(1024)
            var nRead: Int
            while (inputStream.read(data, 0, data.size).also { nRead = it } != -1) {
                buffer.write(data, 0, nRead)
            }
            buffer.flush()
            buffer.toByteArray()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

