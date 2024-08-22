package com.example.dbm.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.dbm.data.local.entities.UserEntity
import com.example.dbm.login.presentation.objects.User

@Dao
interface UserDao {
    @Upsert
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT firstName || ' ' || lastName FROM user_table WHERE email = :email")
    suspend fun getUserNameByEmail(email: String): String

    @Query("SELECT * FROM user_table WHERE id = :id")
    suspend fun getUserByUserId(id: String): User
}