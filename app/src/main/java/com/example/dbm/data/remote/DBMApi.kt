package com.example.dbm.data.remote

import com.example.dbm.data.remote.dtos.RegisterUserDTO
import com.example.dbm.data.remote.response_objects.LoginUserResponse
import com.example.dbm.login.domain.objects.Login
import retrofit2.http.Body
import retrofit2.http.POST

interface DBMApi {
    @POST("/register")
    suspend fun registerUser(@Body body: RegisterUserDTO)

    @POST("/login")
    suspend fun loginUser(@Body body: Login): LoginUserResponse
}