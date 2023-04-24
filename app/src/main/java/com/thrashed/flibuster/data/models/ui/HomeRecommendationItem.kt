package com.thrashed.flibuster.data.models.ui

import com.thrashed.flibuster.data.models.search.SearchItem

sealed class HomeRecommendationItem {

    data class HomeItem(val searchItem: SearchItem): HomeRecommendationItem()

    object Loading: HomeRecommendationItem()

    object Error: HomeRecommendationItem()
}
