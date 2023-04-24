package com.thrashed.flibuster.data.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thrashed.flibuster.data.database.converters.*
import com.thrashed.flibuster.data.database.dao.BookDao
import com.thrashed.flibuster.data.database.entities.BookEntity

@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    AuthorConverter::class,
    CategoryConverter::class,
    ContentConverter::class,
    DateConverter::class,
    LinkConverter::class,
    SaveConverter::class
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun booksDao(): BookDao
}