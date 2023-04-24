package com.thrashed.flibuster.data.database.converters

import androidx.room.TypeConverter
import com.thrashed.flibuster.data.base.SaveType

class SaveConverter {
    @TypeConverter
    fun toString(content: SaveType): Int {
        return content.ordinal
    }

    @TypeConverter
    fun toContent(type: Int): SaveType {
        return enumValues<SaveType>()[type]
    }
}