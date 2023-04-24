package com.thrashed.flibuster.data.api

import com.thrashed.flibuster.data.models.suggestions.SuggestionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SuggestApi {

    @GET("search")
    suspend fun suggestions(
        @Query("types") bookTypes: String? = "text_book",
        @Query("q") q: String?
    ): Response<SuggestionResponse>

    @GET("search")
    suspend fun authorSuggestion(
        @Query("types") bookTypes: String? = "person",
        @Query("q") q: String?
    ): Response<SuggestionResponse>
}