package com.thrashed.flibuster.data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @OnboardingPreferencies
    fun onboardingSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("onboarding_preferences", Context.MODE_PRIVATE)
    }
}