package com.thrashed.flibuster.data.models.ui

import com.thrashed.flibuster.data.database.entities.BookEntity

sealed class HomeHistoryItem {

    class Book(val bookItem: BookEntity): HomeHistoryItem()

    object Empty: HomeHistoryItem()

}
