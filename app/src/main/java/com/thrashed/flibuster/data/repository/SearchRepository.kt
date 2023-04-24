package com.thrashed.flibuster.data.repository

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.thrashed.flibuster.data.api.FlibustApi
import com.thrashed.flibuster.data.base.BadRequestException
import com.thrashed.flibuster.data.base.SingleOrListAdapterFactory
import com.thrashed.flibuster.data.models.search.SearchData
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val flibustApi: FlibustApi
) {

    suspend fun search(query: String, page: Int = 0) = withContext(Dispatchers.IO) {
        val response = flibustApi.search(query, page)
        if (response.isSuccessful) {
            val xmlToJson = XmlToJson.Builder(response.body() ?: "").build()
            val string = xmlToJson.toString()
            val moshi = Moshi.Builder().add(SingleOrListAdapterFactory).build()
            val adapter: JsonAdapter<SearchData> = moshi.adapter(
                SearchData::class.java
            )
            val result = adapter.fromJson(string)?.feed
            return@withContext result
        } else {
            throw BadRequestException()
        }
    }
}