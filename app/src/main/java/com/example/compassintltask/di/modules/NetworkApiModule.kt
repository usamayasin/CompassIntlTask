package com.example.compassintltask.di.modules

import android.app.Application
import com.example.compassintltask.BuildConfig
import com.example.compassintltask.data.local.repository.LocalRepository
import com.example.compassintltask.data.remote.service.ApiService
import com.example.compassintltask.data.remote.ConnectivityInterceptor
import com.example.compassintltask.data.remote.service.ApiServiceImpl
import com.example.compassintltask.utils.Constants
import com.example.compassintltask.utils.Constants.Network.HEADER_API_KEY
import com.example.compassintltask.utils.SessionManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * The DI Module to provide the instances of [OkHttpClient], [Retrofit], and [ApiService] classes.
 */

@Module
@InstallIn(SingletonComponent::class)
class NetworkApiModule {

    @Singleton
    @Provides
    fun provideOKHttpClient(): OkHttpClient {

        // Logging interceptor
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val apiInterceptor = Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader(
                        HEADER_API_KEY, BuildConfig.API_KEY
                    ).addHeader("Authorization", "Bearer mockToken")
                    .build()
            )
        }

        return OkHttpClient.Builder()
            .readTimeout(Constants.Network.TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(Constants.Network.TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(apiInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ConnectivityInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    @Singleton
    @Provides
    fun providesApiService(
        localRepository: LocalRepository,
        application: Application
    ): ApiService {
        return ApiServiceImpl(
            localRepository = localRepository,
            sessionManager = SessionManager(application)
        )
    }
}
