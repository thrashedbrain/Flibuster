package com.thrashed.flibuster.ui.onboarding

import android.content.Context
import androidx.lifecycle.*
import com.thrashed.flibuster.R
import com.thrashed.flibuster.data.models.ui.OnboardActionType
import com.thrashed.flibuster.data.models.ui.OnboardingItem
import com.thrashed.flibuster.data.repository.BottomNavRepository
import com.thrashed.flibuster.data.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingVM @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bottomNavRepository: BottomNavRepository,
    private val onboardingRepository: OnboardingRepository
): ViewModel() {

    private val _permissionRequest = MutableLiveData<Boolean>()
    val permissionRequest: LiveData<Boolean> = _permissionRequest.distinctUntilChanged()

    private val _onboardingItems = MutableLiveData<List<OnboardingItem>>()
    val onboardingItems: LiveData<List<OnboardingItem>> = _onboardingItems

    fun initOnboarding() {
        _onboardingItems.value = getOnboardingList()
    }

    fun setOnboardingState() = viewModelScope.launch {
        onboardingRepository.saveOnboardingState()
    }

    fun showOnboarding(isVisible: Boolean) {
        bottomNavRepository.setBottomNavigationVisibility(isVisible)
    }

    private fun getOnboardingList(): List<OnboardingItem> {
        return listOf(
            OnboardingItem(
                "Привет!",
                "Рады представить вам бесконечную библиотеку книг!",
                context.getDrawable(R.drawable.img_birds),
                OnboardActionType.NONE
            ),
            OnboardingItem(
                "Попробуйте!",
                "Мы постарались сделать удобный инструмент для flibusta, чтобы вам было удобно искать книги",
                context.getDrawable(R.drawable.img_tea),
                OnboardActionType.NONE
            ),
            OnboardingItem(
                "Последний шаг",
                "Нам нужно получить ваше разрешение на чтение и запись, чтобы вы могли скачивать книги",
                context.getDrawable(R.drawable.img_bookshelf),
                OnboardActionType.PERMISSION
            ),
        )
    }
}