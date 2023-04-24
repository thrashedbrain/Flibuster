package com.thrashed.flibuster.data.models.search

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.thrashed.flibuster.data.base.SingleOrList
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class SearchData(
    val feed: SearchFeed?
)

@JsonClass(generateAdapter = true)
data class SearchFeed(
    @SingleOrList
    val entry: List<SearchItem>?,
    @Json(name = "os:totalResults") val totalResults: Int?,
    @Json(name = "os:startIndex") val startIndex: Int?,
    @Json(name = "os:itemsPerPage") val itemsPerPage: Int?
)

@JsonClass(generateAdapter = true)
@Parcelize
data class SearchItem(
    @SingleOrList
    val author: List<SearchAuthorItem>?,
    @Json(name = "dc:language") val language: String?,
    @Json(name = "dc:format") val format: String?,
    @Json(name = "dc:issued") val year: String?,
    @SingleOrList
    val link: List<SearchLinkItem>?,
    val title: String?,
    @SingleOrList
    val category: List<SearchCategoryItem>?,
    val content: SearchContentItem?,
    val id: String?
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class SearchAuthorItem(val name: String?): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class SearchLinkItem(val rel: String?, val href: String?, val type: String?, val title: String?): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class SearchCategoryItem(val term: String?, val title: String?): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class SearchContentItem(val content: String?): Parcelable