package com.example.dbm.job.di

import android.content.Context
import com.example.dbm.data.local.daos.JobDao
import com.example.dbm.data.local.database.DbmDatabase
import com.example.dbm.data.remote.DBMApi
import com.example.dbm.job.data.JobRepositoryImpl
import com.example.dbm.job.domain.JobRepository
import com.example.dbm.job.domain.SaveJobUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JobScreenModule {

    @Provides
    @Singleton
    fun provideJobDao(db: DbmDatabase): JobDao {
        return db.jobDao()
    }

    @Provides
    @Singleton
    fun provideJobRepository(
        dbmApi: DBMApi,
        jobDao: JobDao,
        @ApplicationContext context: Context
    ): JobRepository {
        return JobRepositoryImpl(dbmApi,jobDao, context)
    }

    @Provides
    @Singleton
    fun provideSaveJobUseCase(jobRepository: JobRepository): SaveJobUseCase {
        return SaveJobUseCase(jobRepository)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context) : Context {
        return context
    }
}