package com.example.dbm.job.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.dbm.data.local.daos.JobDao
import com.example.dbm.data.local.entities.JobEntity
import com.example.dbm.data.remote.DBMApi
import com.example.dbm.data.remote.dtos.DeleteJobRequest
import com.example.dbm.data.remote.dtos.JobDTO
import com.example.dbm.data.remote.dtos.PhotoDto
import com.example.dbm.data.remote.response_objects.UploadJobResponseObject
import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.LocalDataErrorHelper
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.domain.JobRepository
import com.example.dbm.job.domain.objects.JobData
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.job.presentation.objects.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val dbmAPi: DBMApi,
    private val jobDao: JobDao,
    private val context: Context
): JobRepository {

    suspend fun upload(jobDTO: JobDTO) {
        try {
            val response = dbmAPi.uploadNewJob(jobDTO)

            if (response.isSuccessful) {
                val rawJson = response.body()?.string()  // ✅ Get raw JSON
                Log.d("API_RAW_RESPONSE", "Response JSON: $rawJson")
            } else {
                Log.e("API_ERROR", "Error response: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Unexpected error: ${e.message}", e)
        }

    }

    override suspend fun saveJob(job: Job): Result<Boolean, DataError.Network> {
        return try {
            val jobDTO = job.toJobDto(context)
            upload(jobDTO)
            val photoUrls: List<UploadJobResponseObject> = dbmAPi.uploadNewJob(jobDTO)
            Log.d("Raw API Response", "saveJob: $photoUrls")
            if (photoUrls.isNotEmpty()) {
                val updatedPhotoList = job.photoList?.map { photo ->
                    val matchedResponse = photoUrls.find { it.questionId == photo.questionIds }
                    photo.copy(photoUrl = matchedResponse?.photoUrl)
                }
                val updatedJob = job.copy(photoList = updatedPhotoList)
                saveJobToDB(updatedJob)
            } else {
                saveJobToDB(job)
            }
            Result.Success(true)
        } catch (e: HttpException) {
            LocalDataErrorHelper.determineNetworkDataErrorMessage(e.code())
        } as Result<Boolean, DataError.Network>
    }

    private suspend fun saveJobToDB(job: Job): Result<Boolean, DataError.Local> {
        return try {
            jobDao.insertJob(job.toJobEntity())
            Result.Success(true)
        } catch (e: IOException) {
            LocalDataErrorHelper.determineLocalDataErrorMessage(e.message ?: "")
        } as Result<Boolean, DataError.Local>
    }

    override suspend fun getJobDataByJobId(jobId: String): Result<JobData, DataError.Local> {
        return try {
            val jobData = jobDao.getJobDataByFormId(jobId)
            Result.Success(data = jobData)
        } catch (e: IOException) {
            LocalDataErrorHelper.determineLocalDataErrorMessage(e.message ?: "")
        } as Result<JobData, DataError.Local>
    }

    override suspend fun getJobByJobId(jobId: String): Result<Boolean, DataError.Local> {
        return try {
            val wasSubmitted = jobDao.getJobByFormId(jobId)
            if (wasSubmitted == 1) {
                Result.Success(true)
            } else {
                Result.Success(false)
            }
        } catch (e: IOException) {
            LocalDataErrorHelper.determineLocalDataErrorMessage(e.message ?: "")
        } as Result<Boolean, DataError.Local>
    }

    override fun getUnsubmittedJobsFromDB(): Result<Flow<List<Job>>, DataError.Local> {
        return try {
            val job: Flow<List<Job>> = jobDao.getUnfinishedJobs().map { jobList ->
                jobList.map { it.toJob() }
            }
            Result.Success(job)
        } catch (e: IOException) {
            LocalDataErrorHelper.determineLocalDataErrorMessage(e.message ?: "")
        } as Result<StateFlow<List<Job>>, DataError.Local>
    }

    override fun getJobsFromDB(userId: String): Result<Flow<List<Job>>, DataError.Local> {
        return try {
            val job: Flow<List<Job>> = jobDao.getSubmittedJobs().map { jobList ->
                jobList.map { it.toJob() }
            }
            Result.Success(job)
        } catch (e: IOException) {
            LocalDataErrorHelper.determineLocalDataErrorMessage(e.message ?: "")
        } as Result<Flow<List<Job>>, DataError.Local>
    }

    override suspend fun deleteUnsubmittedJobsFromDB(jobId: String): Result<Boolean, DataError.Local> {
        return try {
            jobDao.deleteJob(jobId)
            Result.Success(true)
        } catch (e: IOException) {
            LocalDataErrorHelper.determineLocalDataErrorMessage(e.message ?: "")
        } as Result<Boolean, DataError.Local>
    }

    override suspend fun deleteUnsubmittedJobsFromAPI(jobId: String): Result<Boolean, DataError.Network> {
        return try {
            val request = DeleteJobRequest(jobId)
            dbmAPi.deleteJob(request)
            Result.Success(true)
        } catch (e: HttpException) {
            LocalDataErrorHelper.determineNetworkDataErrorMessage(e.code())
        } as Result<Boolean, DataError.Network>
    }

    private fun JobEntity.toJob(): Job {
        return Job(
            formId = formId,
            email = email,
            name = "",
            userId = userId,
            phoneNumber = phoneNumber,
            companyName = companyName,
            companyAddress = companyAddress,
            dateCreated = dateCreated,
            questionsAndAnswers = questionList,
            photoList = photoList,
            wasSubmitted = wasSubmitted
        )
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
            photoList = photoList
        )
    }

    private fun Job.toJobDto(context: Context): JobDTO {
        val photoDtoList = mutableListOf<PhotoDto>()
        photoList?.takeIf { it.isNotEmpty() }?.forEach { photo ->
            val photoDto = photo.photo?.let { byteArray ->
                getImageDataFromUri(byteArray, context)?.let { byteArray ->
                    // Convert each byte to an unsigned integer (0 to 255)
                    PhotoDto(
                        photo = byteArray,
                        questionIds = photo.questionIds
                    )
                }
            }
            if (photoDto != null) {
                photoDtoList.add(photoDto)
            }
        }
        val questionDic = questionsAndAnswers.toDictionary()

        return JobDTO(
            formId = formId!!,
            companyAddress = companyAddress ?: "",
            companyName = companyName ?: "",
            createdBy = userId ?: "",
            dateCreated = dateCreated.toString(),
            questionsAndAnswers = questionDic,
            photoList = photoDtoList,
            email = email!!,
            name = name ?: "",
            phoneNumber = phoneNumber ?: "",
            wasSubmitted = wasSubmitted ?: false
        )
    }


    private fun List<Question>?.toDictionary(): List<Map<String, Any?>> {
        return this?.map { question ->
            mapOf(
                "questionText" to question.questionText,
                "questionId" to question.questionId?.toString(),  // Convert to string if needed
                "answer" to question.answer,
            )
        } ?: emptyList()
    }

    private fun getImageDataFromUri(photo: Uri, context: Context): ByteArray? {
            return try {
                context.contentResolver.openInputStream(photo)?.use { inputStream ->
                    inputStream.readBytes()  // Directly return ByteArray
                }
            } catch (e: Exception) {
                Log.e("PhotoConversion", "Error converting URI to ByteArray", e)
                null
            }
        }
    }

