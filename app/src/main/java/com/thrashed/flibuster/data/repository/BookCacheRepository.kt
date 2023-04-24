package com.thrashed.flibuster.data.repository

import com.thrashed.flibuster.data.database.dao.BookDao
import com.thrashed.flibuster.data.database.entities.BookEntity
import com.thrashed.flibuster.data.database.entities.toBookEntity
import com.thrashed.flibuster.data.models.search.SearchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class BookCacheRepository @Inject constructor(
    private val bookDao: BookDao
) {
    fun getFavoritesBooksFlow() = bookDao.getFavoritesBooksFlow()

    fun getDownloadBooksFlow() = bookDao.getDownloadedBooksFlow()

    fun getBookFlow(searchItem: SearchItem?) = bookDao.getBooksByIdFlow(searchItem?.id ?: "")

    fun getFavoriteBookFlow(searchItem: SearchItem?) =
        bookDao.getFavoritesBooksByIdFlow(searchItem?.id ?: "")

    fun getDownloadedBookFlow(searchItem: SearchItem?) =
        bookDao.getDownloadedBooksByIdFlow(searchItem?.id ?: "")

    suspend fun getCachedBooks() =
        withContext(Dispatchers.IO) { return@withContext bookDao.getBooks() }

    private suspend fun getFavoriteBookEntity(searchItem: SearchItem) =
        withContext(Dispatchers.IO) {
            val book = bookDao.getBooksById(searchItem.id ?: return@withContext null)
            return@withContext book?.firstOrNull()
        }

    suspend fun insertFavoriteBook(searchItem: SearchItem) = withContext(Dispatchers.IO) {
        val item = bookDao.getBookById(searchItem.id)
        if (item == null) {
            bookDao.insert(searchItem.toBookEntity().apply {
                isFavorite = true
                favoriteDate = Date().time
            })
        } else {
            bookDao.update(item.apply {
                isFavorite = true
                favoriteDate = Date().time
            }
            )
        }
    }

    suspend fun deleteFavoriteBook(searchItem: SearchItem) = withContext(Dispatchers.IO) {
        val item = bookDao.getBookById(searchItem.id) ?: return@withContext
        if (item.isDownloaded == true) {
            bookDao.update(item.apply {
                isFavorite = false
                favoriteDate = null
            })
        } else {
            bookDao.delete(item)
        }
    }

    suspend fun insertDownloadBook(bookEntity: BookEntity) = withContext(Dispatchers.IO) {
        val item = bookDao.getBookById(bookEntity.bookId)
        if (item == null) {
            bookDao.insert(bookEntity)
        } else {
            bookDao.update(item.apply {
                isDownloaded = true
                downloadDate = Date().time
                fileUri = bookEntity.fileUri
                mimetype = bookEntity.mimetype
                status = bookEntity.status
                downloadId = bookEntity.downloadId
            })
        }
    }
}