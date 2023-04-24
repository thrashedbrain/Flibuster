package com.thrashed.flibuster.data.di

import com.thrashed.flibuster.data.api.FlibustApi
import com.thrashed.flibuster.data.api.SuggestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    @WebserviceData
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://flib.flibusta.is/opds/")
        .client(
            OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideDataApi(@WebserviceData retrofit: Retrofit): FlibustApi = retrofit.create(FlibustApi::class.java)

    @Provides
    @Singleton
    @WebserviceSuggestions
    fun providesSuggestionRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.litres.ru/foundation/api/")
        .client(
            OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSuggestionApi(@WebserviceSuggestions retrofit: Retrofit): SuggestApi = retrofit.create(SuggestApi::class.java)
}