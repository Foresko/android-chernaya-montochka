package com.foresko.LoanCalculator.di

import com.foresko.LoanCalculator.core.rest.MicroloansRequest
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
            .baseUrl("https://microloans.hb.ru-msk.vkcs.cloud/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePurchaseSubscriptionInfo(retrofit: Retrofit): MicroloansRequest {
        return retrofit.create(MicroloansRequest::class.java)
    }
}
