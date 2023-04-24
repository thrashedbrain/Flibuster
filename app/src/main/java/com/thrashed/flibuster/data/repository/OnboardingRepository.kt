package com.thrashed.flibuster.data.repository

import android.content.SharedPreferences
import com.thrashed.flibuster.data.di.OnboardingPreferencies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OnboardingRepository @Inject constructor(
    @OnboardingPreferencies private val onboardingPreferencies: SharedPreferences
) {

    suspend fun saveOnboardingState() = withContext(Dispatchers.IO) {
        onboardingPreferencies.edit().putBoolean(ONBOARDING_KEY, true).apply()
    }

    suspend fun getOnboardingState() = withContext(Dispatchers.IO) {
        return@withContext onboardingPreferencies.getBoolean(ONBOARDING_KEY, false)
    }

    companion object {
        private const val ONBOARDING_KEY = "onboarding"
    }
}