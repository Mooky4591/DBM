package com.example.dbm.domain

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class App : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
}