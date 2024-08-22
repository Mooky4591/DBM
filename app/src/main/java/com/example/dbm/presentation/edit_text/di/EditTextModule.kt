package com.example.dbm.presentation.edit_text.di

import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.data.remote.DBMApi
import com.example.dbm.presentation.edit_text.data.EditTextRepositoryImpl
import com.example.dbm.presentation.edit_text.domain.EditTextRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EditTextModule {

    @Provides
    @Singleton
    fun provideEditTextRepository(
        userDao: UserDao,
        retrofit: DBMApi
    ) : EditTextRepository {
        return EditTextRepositoryImpl(userDao, retrofit)
    }
}