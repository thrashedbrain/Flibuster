package com.thrashed.flibuster.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thrashed.flibuster.data.models.search.SearchCategoryItem
import java.lang.reflect.Type


class CategoryConverter {

    @TypeConverter
    fun toString(author: List<SearchCategoryItem>?): String? {
        return if (author.isNullOrEmpty()) {
            null
        } else {
            val gson = Gson()
            val type: Type = object : TypeToken<List<SearchCategoryItem>?>() {}.type
            gson.toJson(author, type)
        }
    }

    @TypeConverter
    fun toCategoryList(string: String?): List<SearchCategoryItem>? {
        return if (string.isNullOrBlank()) {
            null
        } else {
            val gson = Gson()
            val type: Type = object : TypeToken<List<SearchCategoryItem>?>() {}.type
            gson.fromJson(string, type)
        }
    }
}