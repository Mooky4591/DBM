package com.example.dbm.job_search.di

import com.example.dbm.job.domain.JobRepository
import com.example.dbm.job_search.data.SearchScreenRepositoryImpl
import com.example.dbm.job_search.domain.SearchScreenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchScreenModule {

    @Provides
    @Singleton
    fun provideSearchScreenRepository(
        jobRepository: JobRepository
    ): SearchScreenRepository {
        return SearchScreenRepositoryImpl(jobRepository)
    }
}