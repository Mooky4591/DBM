package com.example.dbm.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.dbm.data.local.entities.UserEntity

@Dao
interface UserDao {
    @Upsert
    suspend fun insertUser(userEntity: UserEntity)

    @Query("Select firstName || ' ' || lastName FROM user_table WHERE email = :email")
    suspend fun getUserNameByEmail(email: String): String
}