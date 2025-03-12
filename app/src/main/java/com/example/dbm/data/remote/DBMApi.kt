package com.example.dbm.data.remote

import com.example.dbm.data.remote.dtos.DeleteJobRequest
import com.example.dbm.data.remote.dtos.JobDTO
import com.example.dbm.data.remote.dtos.RegisterUserDTO
import com.example.dbm.data.remote.dtos.UpdateUserDTO
import com.example.dbm.data.remote.response_objects.LoginUserApiResponse
import com.example.dbm.login.presentation.objects.Login
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DBMApi {

    //user methods
    @POST("/prod/registerUser")
    suspend fun registerUser(@Body body: RegisterUserDTO)

    @POST("/prod/login")
    suspend fun loginUser(@Body body: Login): Response<LoginUserApiResponse>

    @POST("/prod/updateUser")
    suspend fun updateUser(@Body body: UpdateUserDTO)

    @POST("/prod/changePassword")
    suspend fun updateUserPassword(@Body email: String, password: String)

    //job methods
    @POST("/prod/newJob")
    suspend fun uploadNewJob(@Body job: JobDTO): Response<ResponseBody>

    @POST("/prod/deleteJob")
    suspend fun deleteJob(@Body request: DeleteJobRequest)
}