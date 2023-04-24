package com.thrashed.flibuster.data.models.ui

import android.graphics.drawable.Drawable

data class OnboardingItem(
    val title: String,
    val description: String?,
    val drawable: Drawable?,
    val actionType: OnboardActionType
)

enum class OnboardActionType {
    NONE,
    PERMISSION
}
