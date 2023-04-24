package com.thrashed.flibuster.main

import androidx.lifecycle.*
import com.thrashed.flibuster.data.repository.BottomNavRepository
import com.thrashed.flibuster.data.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val bottomNavRepository: BottomNavRepository,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _showOnboarding = MutableLiveData<Boolean>()
    val showOnboarding: LiveData<Boolean> = _showOnboarding.distinctUntilChanged()

    fun initOnboarding() = viewModelScope.launch {
        val isNeedOnboardingShow = onboardingRepository.getOnboardingState()
        _showOnboarding.value = isNeedOnboardingShow
    }

    val bottomNavVisibility: LiveData<Boolean> =
        bottomNavRepository.observeBottomNavigationBarVisible().asLiveData()

    fun setUpCommand() {
        bottomNavRepository.setUpCommand()
    }
}