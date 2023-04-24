package com.thrashed.flibuster.ui.book

import android.app.DownloadManager
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.thrashed.flibuster.data.database.entities.BookEntity
import com.thrashed.flibuster.data.models.search.SearchItem
import com.thrashed.flibuster.data.repository.BookCacheRepository
import com.thrashed.flibuster.data.repository.BottomNavRepository
import com.thrashed.flibuster.data.utils.BookDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BookVM @Inject constructor(
    private val bookCacheRepository: BookCacheRepository,
    private val bottomNavRepository: BottomNavRepository,
    private val downloader: BookDownloader
) : ViewModel() {

    fun getBook(searchItem: SearchItem?) : LiveData<BookEntity?> = bookCacheRepository
        .getBookFlow(searchItem)
        .asLiveData(Dispatchers.IO)

    fun getDownloadedBook(searchItem: SearchItem?) : LiveData<List<BookEntity>?> = bookCacheRepository
        .getDownloadedBookFlow(searchItem)
        .asLiveData(Dispatchers.IO)

    fun addFavoriteBook(searchItem: SearchItem?) = viewModelScope.launch {
        bookCacheRepository.insertFavoriteBook(searchItem ?: return@launch)
    }

    fun deleteFavoriteBook(searchItem: SearchItem?) = viewModelScope.launch {
        bookCacheRepository.deleteFavoriteBook(searchItem ?: return@launch)
    }

    fun setBottomNavVisibility(visible: Boolean) {
        bottomNavRepository.setBottomNavigationVisibility(visible)
    }

    fun downloadBook(searchItem: SearchItem) {
        downloader.downloadBook(searchItem)
    }

    fun checkFile(fileUri: String): Boolean {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileUri)
        return file.exists()
    }

    fun checkFileStatus(bookEntity: BookEntity?) = bookEntity != null
            && bookEntity.status != DownloadManager.STATUS_SUCCESSFUL
            && bookEntity.status != DownloadManager.STATUS_FAILED

    fun checkDownloadedFile(bookEntity: BookEntity?) = bookEntity != null
            && bookEntity.status == DownloadManager.STATUS_SUCCESSFUL
            && checkFile(bookEntity.fileUri ?: "")
}