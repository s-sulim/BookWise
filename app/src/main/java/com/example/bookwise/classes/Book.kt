package com.example.bookwise.classes

class Book {
    var id: Int = 0
    var title: String? = ""
    var author: String? = ""
    var pages: String = ""
    var genre: String = ""
    var readingTime: String = ""
    var personalRating: String = ""
    var isRead: Boolean = false
    var favoriteIdea: String = ""
    var theme: String = ""
    var quotes:MutableList<String> = mutableListOf()

    constructor(
        title: String?,
        author: String?,
        isRead: Boolean
    ) {
        this.title = title
        this.author = author
        this.isRead = isRead
    }
    constructor(
        id: Int,
        title: String,
        author: String,
        isRead: Boolean,
        pages: String?,
        genre: String?,
        readingTime: String?,
        personalRating: String?,
        favoriteIdea: String?,
        theme: String?
    ) {
        this.id = id
        this.title = title
        this.author = author
        this.isRead = isRead

        if (pages != null) {
            this.pages = pages
        }

        if (genre != null) {
            this.genre = genre
        }
        if (readingTime != null) {
            this.readingTime = readingTime
        }
        if (personalRating != null) {
            this.personalRating = personalRating
        }
        if (favoriteIdea != null) {
            this.favoriteIdea = favoriteIdea
        }
        if (theme != null) {
            this.theme = theme
        }
    }
}