package com.thrashed.flibuster.data.models.search

enum class SearchLinkType(val value: String) {
    TITLE("http://opds-spec.org/image"),
    DOWNLOAD("http://opds-spec.org/acquisition/open-access"),
    FB2("application/fb2+zip"),
    EPUB("application/epub+zip")
}