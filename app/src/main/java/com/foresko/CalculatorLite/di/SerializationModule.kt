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
            .baseUrl("https://financial-apps.hb.ru-msk.vkcs.cloud/microloans/s_microloans_calculator_first/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePurchaseSubscriptionInfo(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}