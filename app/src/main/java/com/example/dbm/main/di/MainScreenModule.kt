package com.example.dbm.main.di

import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.job.domain.JobRepository
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
        jobRepository: JobRepository
    ): MainScreenRepository {
        return MainScreenRepositoryImpl(userDao, jobRepository)
    }
}