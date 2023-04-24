package com.thrashed.flibuster.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thrashed.flibuster.data.models.search.*
import java.util.Date

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val date: Long?,
    val author: List<SearchAuthorItem>?,
    val language: String?,
    val format: String?,
    val year: String?,
    val title: String?,
    val content: SearchContentItem?,
    val link: List<SearchLinkItem>?,
    val category: List<SearchCategoryItem>?,
    val bookId: String?,
    var downloadId: Long?,
    var status: Int?,
    var mimetype: String?,
    var fileUri: String?,
    var isDownloaded: Boolean?,
    var downloadDate: Long?,
    var isFavorite: Boolean?,
    var favoriteDate: Long?
)

fun BookEntity.toBookItem(): SearchItem =
    SearchItem(author, language, format, year, link, title, category, content, bookId)

fun SearchItem.toBookEntity(): BookEntity =
    BookEntity(
        date = Date().time,
        author = author,
        language = language,
        format = format,
        year = year,
        title = title,
        content = content,
        link = link,
        category = category,
        bookId = id,
        downloadId = null,
        status = null,
        mimetype =  null,
        fileUri = null,
        isDownloaded = null,
        isFavorite = null,
        downloadDate = null,
        favoriteDate = null
    )