package com.thrashed.flibuster.data.models.suggestions

import com.google.gson.annotations.SerializedName

data class SuggestionInstanceItem(
    val title: String?,
    @SerializedName("full_name") val fullName: String?
)

data class SuggestionItem(val type: String?, val instance: SuggestionInstanceItem?)

data class SuggestionPayload(val data: List<SuggestionItem>?)

data class SuggestionResponse(val payload: SuggestionPayload)