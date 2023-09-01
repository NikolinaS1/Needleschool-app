package com.example.sewinglessons.di

import com.example.sewinglessons.data.api.ApiConstants
import com.example.sewinglessons.data.api.SewingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SewingApiModule {
    @Provides
    @Singleton
    fun provideApi(builder: Retrofit.Builder): SewingApi {
        return builder
            .build()
            .create(SewingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
    }
}