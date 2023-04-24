package com.thrashed.flibuster.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thrashed.flibuster.data.models.search.SearchAuthorItem
import java.lang.reflect.Type


class AuthorConverter {

    @TypeConverter
    fun toString(author: List<SearchAuthorItem>?): String? {
        return if (author.isNullOrEmpty()) {
            null
        } else {
            val gson = Gson()
            val type: Type = object : TypeToken<List<SearchAuthorItem>?>() {}.type
            gson.toJson(author, type)
        }
    }

    @TypeConverter
    fun toAuthorList(string: String?): List<SearchAuthorItem>? {
        return if (string.isNullOrBlank()) {
            null
        } else {
            val gson = Gson()
            val type: Type = object : TypeToken<List<SearchAuthorItem>?>() {}.type
            gson.fromJson(string, type)
        }
    }
}