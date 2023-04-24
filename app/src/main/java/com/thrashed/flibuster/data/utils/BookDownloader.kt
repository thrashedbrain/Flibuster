package com.thrashed.flibuster.data.utils

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.thrashed.flibuster.data.base.Consts
import com.thrashed.flibuster.data.database.entities.toBookEntity
import com.thrashed.flibuster.data.models.search.SearchItem
import com.thrashed.flibuster.data.models.search.SearchLinkItem
import com.thrashed.flibuster.data.models.search.SearchLinkType
import com.thrashed.flibuster.data.repository.BookCacheRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.util.Date
import javax.inject.Inject

class BookDownloader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bookCacheRepository: BookCacheRepository
) {

    private val downloadJob = Job()
    private val downloadScope = CoroutineScope(Dispatchers.IO + downloadJob)
    private val downloadManager by lazy { context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }

    fun checkFile(item: SearchItem): String {
        var initFileName = getFileName(item)
        var file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            initFileName)
        var index = 0
        val filename = file.nameWithoutExtension
        val extension = file.extension
        while (file.exists()) {
            val newFileName = "$filename$index.$extension"
            file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                newFileName)
            index++
        }
        Log.d("asd123asd", "asd $file")
        return file.name
    }

    fun downloadBook(item: SearchItem) {
        val filename = checkFile(item)
        getFileLink(item)?.let {

            val link = "${Consts.BASE_API}${it.href}"
            downloadScope.launch {
                try {
                    val url = checkLink(link)
                    val uri = Uri.parse(url)
                    initDownload(item, uri, filename)
                } catch (e: Exception) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun checkLink(url: String): String = withContext(Dispatchers.IO) {
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder().get().url(url).build()

        val response = okHttpClient.newCall(request).execute()
        return@withContext if ((response.body?.contentLength() ?: 0) > 0) {
            url
        } else {
            "$url/"
        }
    }

    @SuppressLint("Range")
    private fun initDownload(searchItem: SearchItem, uri: Uri, filename: String) {
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverRoaming(true).setAllowedOverMetered(true).setTitle(searchItem.title)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, filename
            )
        request.setMimeType("*/*")
        val downloadId = downloadManager.enqueue(request)
        downloadScope.launch {
            var isDownloaded = false
            var status: Int

            while (!isDownloaded) {
                val cursor: Cursor =
                    downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
                if (cursor.moveToFirst()) {
                    status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            onDownloadEnded(searchItem, filename, downloadId, status, true)
                            isDownloaded = true

                        }
                        DownloadManager.STATUS_FAILED -> {
                            onDownloadEnded(searchItem, filename, downloadId, status, false)
                            isDownloaded = true
                        }
                    }
                } else {
                    isDownloaded = true
                }
            }

        }
    }

    private suspend fun onDownloadEnded(searchItem: SearchItem,
                                        filename: String,
                                        downloadId: Long,
                                        downloadStatus: Int,
                                        isSuccess: Boolean) {

        val entity = searchItem.toBookEntity()
        entity.downloadId = downloadId
        entity.status = downloadStatus
        entity.fileUri = filename
        entity.mimetype =
            downloadManager.getMimeTypeForDownloadedFile(downloadId)
        entity.isDownloaded = isSuccess
        entity.downloadDate = Date().time
        bookCacheRepository.insertDownloadBook(entity)
    }

    private fun getFileName(item: SearchItem) = "${item.title?.trimStart()?.replace("?", "")}" +
            ".${item.format?.replace("+", ".")}"

    private fun getFileId(linkItem: SearchLinkItem?) = linkItem?.href
        ?.replace("/b/", "")
        ?.split('/')
        ?.firstOrNull()
        ?.toInt()

    private fun getFileLink(item: SearchItem) = item.link?.firstOrNull {
        it.rel == SearchLinkType.DOWNLOAD.value
                && it.type?.contains(item.format ?: "") ?: false
    }
}