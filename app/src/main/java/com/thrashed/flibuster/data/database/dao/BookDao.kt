package com.thrashed.flibuster.data.database.dao

import androidx.room.*
import com.thrashed.flibuster.data.database.entities.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("select * from books  order by date desc")
    suspend fun getBooks(): List<BookEntity>?

    @Query("select * from books where bookId =:id")
    suspend fun getBooksById(id: String): List<BookEntity>?

    @Query("select * from books where bookId =:id")
    fun getFavoritesBooksByIdFlow(id: String): Flow<List<BookEntity>?>

    @Query("select * from books where bookId =:id and isDownloaded =:isDownloaded")
    fun getDownloadedBooksByIdFlow(id: String, isDownloaded: Boolean = true): Flow<List<BookEntity>?>

    @Query("select * from books where bookId =:id")
    fun getBooksByIdFlow(id: String): Flow<BookEntity?>

    @Query("select * from books where isFavorite =:isFavorite order by favoriteDate desc")
    fun getFavoritesBooksFlow(isFavorite: Boolean = true): Flow<List<BookEntity>?>

    @Query("select * from books where isDownloaded =:isDownloaded order by downloadDate desc")
    fun getDownloadedBooksFlow(isDownloaded: Boolean = true): Flow<List<BookEntity>?>

    @Query("select * from books where bookId =:id")
    fun getBookById(id: String?): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookEntity: BookEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(bookEntity: BookEntity)

    @Delete
    suspend fun delete(bookEntity: BookEntity?)

}