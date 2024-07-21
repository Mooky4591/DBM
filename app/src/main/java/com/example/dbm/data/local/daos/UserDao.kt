package com.example.dbm.data.local.daos

import androidx.room.Dao
import androidx.room.Upsert
import com.example.dbm.data.local.entities.UserEntity

@Dao
interface UserDao {
    @Upsert
    suspend fun insertUser(userEntity: UserEntity)

}