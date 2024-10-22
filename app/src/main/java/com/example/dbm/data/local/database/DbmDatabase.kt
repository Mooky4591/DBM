package com.example.dbm.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dbm.data.converters.Converters
import com.example.dbm.data.local.daos.JobDao
import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.data.local.entities.JobEntity
import com.example.dbm.data.local.entities.UserEntity

@Database(
    entities = [UserEntity::class, JobEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DbmDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun jobDao(): JobDao

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