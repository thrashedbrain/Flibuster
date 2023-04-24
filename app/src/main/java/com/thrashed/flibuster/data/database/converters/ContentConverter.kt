package com.thrashed.flibuster.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thrashed.flibuster.data.models.search.SearchContentItem
import java.lang.reflect.Type


class ContentConverter {

    @TypeConverter
    fun toString(content: SearchContentItem?): String? {
        return if (content == null) {
            null
        } else {
            val gson = Gson()
            val type: Type = object : TypeToken<SearchContentItem?>() {}.type
            gson.toJson(content, type)
        }
    }

    @TypeConverter
    fun toContent(string: String?): SearchContentItem? {
        return if (string.isNullOrBlank()) {
            null
        } else {
            val gson = Gson()
            val type: Type = object : TypeToken<SearchContentItem?>() {}.type
            gson.fromJson(string, type)
        }
    }
}