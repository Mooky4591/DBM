package com.example.dbm.authorization.di

import android.content.Context
import androidx.room.Room
import com.example.dbm.authorization.data.AuthRepositoryImpl
import com.example.dbm.authorization.domain.AuthRepository
import com.example.dbm.data.local.daos.UserDao
import com.example.dbm.data.local.database.DbmDatabase
import com.example.dbm.data.remote.DBMApi
import com.example.dbm.error_handling.domain.PasswordValidator
import com.example.dbm.login.domain.objects.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        api: DBMApi
    ): AuthRepository {
        return AuthRepositoryImpl(userDao, api)
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
        return Retrofit.Builder()
            .baseUrl("https://tasky.pl-coding.com/")
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
}