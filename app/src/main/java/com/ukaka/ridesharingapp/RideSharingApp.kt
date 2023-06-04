package com.ukaka.ridesharingapp

import android.app.Application
import com.airbnb.lottie.BuildConfig
import com.ukaka.ridesharingapp.common.ACCESS_TOKEN
import com.ukaka.ridesharingapp.common.dataStore
import com.ukaka.ridesharingapp.domain.usecases.GetAccessToken
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltAndroidApp
class RideSharingApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}