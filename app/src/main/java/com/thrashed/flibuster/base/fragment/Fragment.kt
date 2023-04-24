package com.thrashed.flibuster.base

import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.hideSoftInput() {
    requireActivity().currentFocus?.let {
        val manager = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        manager?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}
