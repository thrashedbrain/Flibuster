package com.thrashed.flibuster.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BottomNavRepository @Inject constructor() {

    private val bottomNavigationBarVisible = MutableStateFlow(true)
    private val upCommand = MutableStateFlow(false)

    fun observeBottomNavigationBarVisible(): Flow<Boolean> {
        return bottomNavigationBarVisible
    }

    fun setBottomNavigationVisibility(isVisible: Boolean) {
        bottomNavigationBarVisible.value = isVisible
    }

    fun observeUpCommand(): Flow<Boolean> {
        return upCommand
    }

    fun setUpCommand() {
        upCommand.value = true
        upCommand.value = false
    }

}