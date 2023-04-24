package com.thrashed.flibuster.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.thrashed.flibuster.data.models.search.SearchItem
import com.thrashed.flibuster.data.repository.SearchRepository
import java.io.IOException

class BookPagingSource constructor(
    private val bookRepository: SearchRepository,
    private val query: String
): PagingSource<Int, SearchItem>() {

    override fun getRefreshKey(state: PagingState<Int, SearchItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItem> {
        val pageIndex = params.key ?: BOOKS_STARTING_PAGE_INDEX
        try {
            val response = bookRepository.search(
                query,
                pageIndex
            )

            val total = response?.totalResults ?: 0
            val itemsPerPage = response?.itemsPerPage ?: 0

            return LoadResult.Page(
                data = response?.entry ?: listOf(),
                prevKey = if (pageIndex == BOOKS_STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = if ((pageIndex + 1) * itemsPerPage < total) pageIndex + 1 else null
            )

        } catch (e: Exception) {
            e.printStackTrace()
            return LoadResult.Error(e)
        } catch (e: IOException) {
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }

    companion object {
        private const val BOOKS_STARTING_PAGE_INDEX = 0
    }
}