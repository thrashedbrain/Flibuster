package com.thrashed.flibuster.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thrashed.flibuster.data.models.search.SearchLinkItem
import java.lang.reflect.Type


class LinkConverter {

    @TypeConverter
    fun toString(author: List<SearchLinkItem>?): String? {
        return if (author.isNullOrEmpty()) {
            null
        } else {
            val gson = Gson()
            val type: Type = object : TypeToken<List<SearchLinkItem>?>() {}.type
            gson.toJson(author, type)
        }
    }

    @TypeConverter
    fun toLinkList(string: String?): List<SearchLinkItem>? {
        return if (string.isNullOrBlank()) {
            null
        } else {
            val gson = Gson()
            val type: Type = object : TypeToken<List<SearchLinkItem>?>() {}.type
            gson.fromJson(string, type)
        }
    }
}