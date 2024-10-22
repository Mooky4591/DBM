package com.example.dbm.main.di

import com.example.dbm.data.local.daos.JobDao
import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.data.local.database.DbmDatabase
import com.example.dbm.main.data.MainScreenRepositoryImpl
import com.example.dbm.main.domain.MainScreenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainScreenModule {

    @Provides
    @Singleton
    fun provideMainScreenRepository(
        userDao: UserDao,
        jobDao: JobDao
    ): MainScreenRepository {
        return MainScreenRepositoryImpl(userDao, jobDao)
    }
}