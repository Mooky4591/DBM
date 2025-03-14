package com.example.dbm.authorization.di

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.Room
import com.example.dbm.authorization.data.AuthRepositoryImpl
import com.example.dbm.authorization.data.LocalDateAdapter
import com.example.dbm.authorization.domain.AuthRepository
import com.example.dbm.data.local.daos.JobDao
import com.example.dbm.data.local.user_preferences.UserPreferencesImpl
import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.data.local.database.DbmDatabase
import com.example.dbm.data.remote.DBMApi
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.error_handling.domain.EmailValidator
import com.example.dbm.error_handling.domain.PasswordValidator
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        jobDao: JobDao,
        api: DBMApi,
        userPreferences: UserPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(userDao, api, userPreferences, jobDao)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(httpClient: OkHttpClient): DBMApi {
        val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, LocalDateAdapter()).create()
        return Retrofit.Builder()
            .baseUrl("https://8wx262zas0.execute-api.us-east-1.amazonaws.com/")
            .addConverterFactory(
                GsonConverterFactory.create(gson)
            )
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .client(httpClient)
            .build()
            .create(DBMApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): DbmDatabase {
        synchronized(this) {
            return Room.databaseBuilder(
                context.applicationContext,
                DbmDatabase::class.java,
                "database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideUserDao(db: DbmDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun providePasswordValidator(): PasswordValidator {
        return PasswordValidator()
    }

    @Provides
    @Singleton
    fun provideEmailValidator(): EmailValidator {
        return EmailValidator()
    }

    @Provides
    @Singleton
    fun provideUserPreferences(sharedPreferences: SharedPreferences): UserPreferences {
        return UserPreferencesImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    }
}