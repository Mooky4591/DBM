package com.example.dbm.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.data.local.entities.FormQuestionsEntity
import com.example.dbm.data.local.entities.FormResponsesEntity
import com.example.dbm.data.local.entities.FormsEntity
import com.example.dbm.data.local.entities.UserEntity

@Database(
    entities = [UserEntity::class, FormsEntity::class, FormResponsesEntity::class, FormQuestionsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DbmDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: DbmDatabase? = null
        fun getDatabase(context: Context): DbmDatabase {
            if(INSTANCE != null) {
                return INSTANCE as DbmDatabase
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DbmDatabase::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}