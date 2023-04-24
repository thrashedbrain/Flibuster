package com.thrashed.flibuster.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.thrashed.flibuster.data.repository.BookCacheRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HistoryVM @Inject constructor(
    bookCacheRepository: BookCacheRepository
) : ViewModel() {

    val books = bookCacheRepository
        .getDownloadBooksFlow()
        .asLiveData(Dispatchers.IO)
}