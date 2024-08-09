package com.foresko.CalculatorLite.di

import com.foresko.CalculatorLite.core.rest.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SerializationModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://alloffers.loan-guide.ru/suren_sarkisov/chernaya_monetka/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePurchaseSubscriptionInfo(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}