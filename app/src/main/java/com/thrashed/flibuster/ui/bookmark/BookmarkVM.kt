package com.thrashed.flibuster.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.thrashed.flibuster.data.repository.BookCacheRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class BookmarkVM @Inject constructor(
    bookCacheRepository: BookCacheRepository
): ViewModel() {

    val books = bookCacheRepository
        .getFavoritesBooksFlow()
        .asLiveData(Dispatchers.IO)
}