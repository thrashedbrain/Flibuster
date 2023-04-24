package com.thrashed.flibuster.data.repository

import com.thrashed.flibuster.data.api.SuggestApi
import com.thrashed.flibuster.data.base.BadRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SuggestionsRepository @Inject constructor(
    private val suggestApi: SuggestApi
) {

    suspend fun suggestion(query: String?) = withContext(Dispatchers.IO) {
        val response = suggestApi.suggestions(q = query)
        if (response.isSuccessful) {
            return@withContext response.body()
        } else {
            throw BadRequestException()
        }
    }

    suspend fun authorSuggestion(query: String?) = withContext(Dispatchers.IO) {
        val response = suggestApi.authorSuggestion(q = query)
        if (response.isSuccessful) {
            return@withContext response.body()
        } else {
            throw BadRequestException()
        }
    }
}