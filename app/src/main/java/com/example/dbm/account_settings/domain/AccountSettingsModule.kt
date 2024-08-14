package com.example.dbm.account_settings.domain

import android.content.Context
import android.content.SharedPreferences
import com.example.dbm.data.local.user_preferences.UserPreferencesImpl
import com.example.dbm.domain.user_preferences.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountSettingsModule {

}