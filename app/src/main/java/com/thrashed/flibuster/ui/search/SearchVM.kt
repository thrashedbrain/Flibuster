package com.thrashed.flibuster.ui.search

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
import com.thrashed.flibuster.data.paging.BookPagingRepository
import com.thrashed.flibuster.data.repository.BottomNavRepository
import com.thrashed.flibuster.data.repository.SearchRepository
import com.thrashed.flibuster.data.repository.SuggestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchVM @Inject constructor(
    private val bottomNavRepository: BottomNavRepository,
    private val bookPagingRepository: BookPagingRepository,
    private val suggestionsRepository: SuggestionsRepository
) : ViewModel() {

    var booksFlow = MutableLiveData<PagingData<SearchItem>>()

    private val _suggestions = MutableLiveData<List<String>?>()
    val suggestions: LiveData<List<String>?> = _suggestions

    private val _state = MutableLiveData(SearchState.BOOK)
    val state: LiveData<SearchState> = _state

    val upCommand = bottomNavRepository.observeUpCommand().asLiveData()

    fun setSearch(query: String?) {
        if (query.isNullOrBlank() && (query?.length ?: 0) < 2) {
            return
        }
        if (state.value == SearchState.BOOK) {
            initSearch(query ?: "")
        }
    }

    fun setSuggestionQuery(query: String?) {
        if (query.isNullOrBlank() && (query?.length ?: 0) < 2) {
            return
        }
        if (_state.value == SearchState.BOOK) {
            initSuggestionQuery(query)
        } else {
            initAuthorSuggestionQuery(query)
        }
    }

    fun setSearchState() {
        if (_state.value == SearchState.BOOK) {
            _state.value = SearchState.AUTHOR
        } else {
            _state.value = SearchState.BOOK
        }
    }

    private fun initAuthorSuggestionQuery(query: String?) {
        try {
            viewModelScope.launch {
                _suggestions.value = suggestionsRepository
                    .authorSuggestion(query)
                    ?.payload
                    ?.data?.mapNotNull { it.instance?.fullName }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initSuggestionQuery(query: String?) {
        try {
            viewModelScope.launch {
                _suggestions.value = suggestionsRepository
                    .suggestion(query)
                    ?.payload
                    ?.data?.mapNotNull { it.instance?.title }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initSearch(query: String) {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = true),
                pagingSourceFactory = { bookPagingRepository.getBooksPagingSource(query) }
            ).flow.cachedIn(viewModelScope).collectLatest {
                booksFlow.value = (it)
            }
        }
    }

    enum class SearchState {
        BOOK, AUTHOR
    }
}