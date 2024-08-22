package com.example.dbm.account_settings.di

import com.example.dbm.account_settings.data.AccountSettingsRepositoryImpl
import com.example.dbm.account_settings.domain.AccountSettingsRepository
import com.example.dbm.data.local.daos.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountSettingsModule {

    @Provides
    @Singleton
    fun provideAccountSettingsRepository(
        userDao: UserDao
    ) : AccountSettingsRepository {
        return AccountSettingsRepositoryImpl(userDao)
    }
}