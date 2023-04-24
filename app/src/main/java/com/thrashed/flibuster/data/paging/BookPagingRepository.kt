package com.thrashed.flibuster.data.paging

import com.thrashed.flibuster.data.repository.SearchRepository
import javax.inject.Inject

class BookPagingRepository @Inject constructor(
    private val searchRepository: SearchRepository
) {

    fun getBooksPagingSource(query: String?) = BookPagingSource(searchRepository, query ?: "")
}