package com.example.dbm.authorization.data

import android.util.Patterns
import com.example.dbm.authorization.domain.AuthRepository
import com.example.dbm.data.local.daos.JobDao
import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.data.local.entities.JobEntity
import com.example.dbm.data.local.entities.UserEntity
import com.example.dbm.data.remote.DBMApi
import com.example.dbm.data.remote.dtos.RegisterUserDTO
import com.example.dbm.data.remote.response_objects.LoginUserResponse
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.login.presentation.objects.Login
import com.example.dbm.login.presentation.objects.User
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val retrofit: DBMApi,
    private val userPref: UserPreferences,
    private val jobDao: JobDao
): AuthRepository {

    override fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override suspend fun loginUser(login: Login): Result<User, DataError.Network> {
        return try {
            val loginUserResponse = retrofit.loginUser(login)
            val responseBody = loginUserResponse.body()
                val userEntity = responseBody?.body?.toUserEntity(
                    responseBody.body.email,
                    responseBody.body.userId,
                    responseBody.body.firstName,
                    responseBody.body.lastName,
                    responseBody.body.companyAddress,
                    responseBody.body.companyName
                )
                for (job in responseBody?.body?.jobs!!) {
                    jobDao.insertJob(job.toJobEntity())
                }
            userDao.insertUser(userEntity!!)
            addUserInfoToUserPrefs(responseBody.body)
            Result.Success(responseBody.body.toUser())
        } catch (e: HttpException) {
            return when (e.code()) {
                408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                409 -> Result.Error(DataError.Network.INCORRECT_PASSWORD_OR_EMAIL)
                429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                500 -> Result.Error(DataError.Network.SERVER_ERROR)
                400 -> Result.Error(DataError.Network.SERIALIZATION)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }        } catch (e: IOException) {
            when (e.message) {
                "UnknownHostException" -> Result.Error(DataError.Network.UNKNOWN_HOST_EXCEPTION)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    private fun addUserInfoToUserPrefs(loginUser: LoginUserResponse) {
        userPref.addUserFullName(loginUser.firstName + " " + loginUser.lastName)
        userPref.addCompanyName(loginUser.companyName)
        userPref.addCompanyAddress(loginUser.companyAddress)
        userPref.addUserPhoneNumber(loginUser.phoneNumber)
    }

    override suspend fun registerUser(user: User, password: String): Result<User, DataError.Network> {
        val userDto = user.toUserDTO(password)
        return try {
            retrofit.registerUser(userDto)
            Result.Success(user)
        } catch (e: HttpException) {
            when (e.code()) {
                408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                409 -> Result.Error(DataError.Network.INCORRECT_PASSWORD_OR_EMAIL)
                429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                500 -> Result.Error(DataError.Network.SERVER_ERROR)
                400 -> Result.Error(DataError.Network.SERIALIZATION)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        } catch (e: IOException) {
            when (e.message) {
                "UnknownHostException" -> Result.Error(DataError.Network.UNKNOWN_HOST_EXCEPTION)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    override suspend fun updatePassword(
        email: String,
        password: String
    ): Result<Boolean, DataError.Network> {
        return try {
            retrofit.updateUserPassword(email, password)
            Result.Success(true)
        } catch (e: HttpException) {
            when (e.code()) {
                408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                409 -> Result.Error(DataError.Network.INCORRECT_PASSWORD_OR_EMAIL)
                429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                500 -> Result.Error(DataError.Network.SERVER_ERROR)
                400 -> Result.Error(DataError.Network.SERIALIZATION)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

}

private fun Job.toJobEntity(): JobEntity {
return JobEntity(
    formId = formId ?: "",
    companyName = companyName ?: "" ,
    companyAddress = companyAddress ?: "",
    dateCreated = dateCreated ?: LocalDate.now(),
    userId = userId ?: "",
    phoneNumber = phoneNumber ?: "",
    email = email ?: "",
    wasSubmitted = wasSubmitted ?: false,
    questionList = questionsAndAnswers ?: emptyList(),
    photoList = photoList ?: emptyList()
)

}

private fun LoginUserResponse.toUserEntity(
    email: String,
    userId: String,
    firstName: String,
    lastName: String,
    companyAddress: String,
    companyName: String): UserEntity {
    return UserEntity(
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        id = userId,
        companyAddress = companyAddress,
        companyName = companyName)
}

private fun LoginUserResponse.toUser(): User {
    return User(
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        userId = userId,
        companyName = companyName,
        companyAddress = companyAddress)
}

private fun User.toUserDTO(password: String): RegisterUserDTO {
    return RegisterUserDTO(
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        password = password,
        companyName = companyName,
        companyAddress = companyAddress)
}
