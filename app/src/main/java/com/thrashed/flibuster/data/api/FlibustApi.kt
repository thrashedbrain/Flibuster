package com.thrashed.flibuster.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlibustApi {

    @GET("opensearch")
    suspend fun search(
        @Query("searchTerm") searchTerm: String?,
        @Query("pageNumber") pageNumber: Int? = 0,
        @Query("searchType") searchTypes: String? = "books"
    ): Response<String>

    @GET("opensearch")
    suspend fun home(
        @Query("searchType") searchTypes: String? = "books"
    ): Response<String>
}