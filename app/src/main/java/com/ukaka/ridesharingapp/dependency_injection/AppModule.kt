package com.ukaka.ridesharingapp.dependency_injection

import android.content.Context
import com.google.maps.GeoApiContext
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ukaka.ridesharingapp.common.ACCESS_TOKEN
import com.ukaka.ridesharingapp.common.Constants.API_BASE_URL
import com.ukaka.ridesharingapp.common.Constants.AUTH_BASE_URL
import com.ukaka.ridesharingapp.common.Constants.MAPS_API_KEY
import com.ukaka.ridesharingapp.common.dataStore
import com.ukaka.ridesharingapp.data.remote.UberAPI
import com.ukaka.ridesharingapp.data.remote.UberAuthService
import com.ukaka.ridesharingapp.data.repository.RideServiceImpl
import com.ukaka.ridesharingapp.domain.models.Validation
import com.ukaka.ridesharingapp.domain.services.RideService
import com.ukaka.ridesharingapp.domain.usecases.GetAccessToken
import com.ukaka.ridesharingapp.domain.usecases.ValidateEmail
import com.ukaka.ridesharingapp.domain.usecases.ValidatePassword
import com.ukaka.ridesharingapp.domain.usecases.ValidateUsername
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** A function to provide a single Context's instance of the application throughout our app */
    @Provides
    @Singleton
    fun provideContextInstance(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun provideGeoApiContext(): GeoApiContext = GeoApiContext
        .Builder()
        .apiKey(MAPS_API_KEY)
        .build()

    @Provides
    @Singleton
    fun provideValidation(): Validation {
        return Validation(
            validateUsername = ValidateUsername(),
            validateEmail = ValidateEmail(),
            validatePassword = ValidatePassword()
        )
    }

    @Provides
    @Singleton
    fun provideRideService(
        uberAPI: UberAPI
    ): RideService {
        return RideServiceImpl(uberAPI)
    }

    @Provides
    @Singleton
    fun provideAccessToken(uberAuthService: UberAuthService): GetAccessToken {
        return GetAccessToken(uberAuthService)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideUberAPI(): UberAPI {
        lateinit var getAccessToken: GetAccessToken
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
        }

        val accessTokenString = getAccessToken().accessToken

        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor{ chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $accessTokenString")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(UberAPI::class.java)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideAuthServiceAPI(): UberAuthService {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(UberAuthService::class.java)
    }
}