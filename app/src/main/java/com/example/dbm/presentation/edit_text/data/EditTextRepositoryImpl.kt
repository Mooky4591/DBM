package com.example.dbm.presentation.edit_text.data

import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.data.local.entities.UserEntity
import com.example.dbm.data.remote.DBMApi
import com.example.dbm.data.remote.dtos.UpdateUserDTO
import com.example.dbm.error_handling.domain.DataError
import com.example.dbm.error_handling.domain.LocalDataErrorHelper
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.login.presentation.objects.User
import com.example.dbm.presentation.edit_text.domain.EditTextRepository
import okio.IOException
import javax.inject.Inject

class EditTextRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val retrofit: DBMApi
): EditTextRepository {

    override suspend fun updateUserToApi(user: User): Result<Boolean, DataError.Network> {
        val userDto = user.toUpdateUserDTO()
        return try {
            retrofit.updateUser(userDto)
            Result.Success(true)
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                500 -> Result.Error(DataError.Network.SERVER_ERROR)
                400 -> Result.Error(DataError.Network.SERIALIZATION)
                else ->Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    override suspend fun updateUserToDB(user: User): Result<User, DataError.Local> {
        val userEntity = user.toUserEntity()
        return try {
            userDao.insertUser(userEntity)
            updateUserToApi(user)
            Result.Success(user)
        } catch (e: IOException) {
            (LocalDataErrorHelper.determineLocalDataErrorMessage(e.message!!))
        } as Result<User, DataError.Local>
    }

    override suspend fun getUser(userId: String): Result<User, DataError.Local> {
        return try {
            val user = userDao.getUserByUserId(userId)
            Result.Success(user)
        }
        catch (e: IOException) {
            when (e.message) {
                "Permission denied" -> Result.Error(DataError.Local.PERMISSION_DENIED)
                "File not found" -> Result.Error(DataError.Local.FILE_NOT_FOUND)
                "Disk full" -> Result.Error(DataError.Local.DISK_FULL)
                "Input/output error" -> Result.Error(DataError.Local.INPUT_OUTPUT_ERROR)
                "Connection refused" -> Result.Error(DataError.Local.CONNECTION_REFUSED)
                else -> Result.Error(DataError.Local.UNKNOWN)
            }
        }
    }

    override suspend fun updatePasswordToApi(email: String, password: String): Result<Boolean, DataError.Network> {
        return try {
            retrofit.updateUserPassword(email, password)
            Result.Success(true)
        }
        catch (e: retrofit2.HttpException) {
            when(e.code()) {
                408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                500 -> Result.Error(DataError.Network.SERVER_ERROR)
                400 -> Result.Error(DataError.Network.SERIALIZATION)
                else ->Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    private fun User.toUserEntity(): UserEntity {
        return UserEntity(
            id = userId ?: "",
            firstName = firstName,
            lastName = lastName,
            email = email,
            phoneNumber = phoneNumber,
            companyName = companyName,
            companyAddress = companyAddress
        )
    }

    private fun User.toUpdateUserDTO(): UpdateUserDTO {
        return UpdateUserDTO(
            userId = userId ?: "",
            firstName = firstName,
            lastName = lastName,
            email = email,
            phoneNumber = phoneNumber,
            companyName = companyName,
            companyAddress = companyAddress
        )
    }

}