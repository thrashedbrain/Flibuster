package com.thrashed.flibuster.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.thrashed.flibuster.data.models.search.SearchItem
import com.thrashed.flibuster.data.models.ui.HomeHistoryItem
import com.thrashed.flibuster.data.paging.BookPagingRepository
import com.thrashed.flibuster.data.repository.BookCacheRepository
import com.thrashed.flibuster.data.repository.BottomNavRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val bookCacheRepository: BookCacheRepository,
    private val bottomNavRepository: BottomNavRepository,
    private val bookPagingRepository: BookPagingRepository
) : ViewModel() {

    private val _homeItems = MutableLiveData<List<SearchItem?>?>()
    val homeItems: LiveData<List<SearchItem?>?> = _homeItems

    private val _homeHistory = MutableLiveData<List<HomeHistoryItem>>()
    val homeHistory: LiveData<List<HomeHistoryItem>> = _homeHistory

    var booksFlow = MutableLiveData<PagingData<SearchItem>>()

    val upCommand = bottomNavRepository.observeUpCommand().asLiveData()

    init {
        initHome()
    }

    fun initHome() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = 20, initialLoadSize = 40, enablePlaceholders = false),
                pagingSourceFactory = { bookPagingRepository.getBooksPagingSource(null) }
            ).flow.cachedIn(viewModelScope).collect {
                booksFlow.value = it
            }
        }
    }

    fun initHistory() {
        try {
            viewModelScope.launch {
                val books = bookCacheRepository.getCachedBooks()
                if (books.isNullOrEmpty()) {
                    _homeHistory.value = listOf(HomeHistoryItem.Empty)
                    return@launch
                }
                _homeHistory.value =
                    books.map { HomeHistoryItem.Book(it) }.distinctBy { it.bookItem.title }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}