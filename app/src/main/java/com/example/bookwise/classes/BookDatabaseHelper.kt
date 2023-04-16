package com.example.bookwise.classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BookDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BooksDatabase.db"

        private const val BOOKS_TABLE_NAME = "BooksTable"
        private const val BOOK_ID = "id"
        private const val BOOK_TITLE = "title"
        private const val BOOK_AUTHOR = "author"
        private const val BOOK_IS_READ = "is_read"
        private const val BOOK_PAGES = "pages"
        private const val BOOK_GENRE = "genre"
        private const val BOOK_READING_TIME = "reading_time"
        private const val BOOK_RATING = "rating"
        private const val BOOK_IDEA = "idea"
        private const val BOOK_THEME = "theme"

        private const val QUOTES_TABLE_NAME = "QuotesTable"
        private const val QUOTE_ID = "id"
        private const val QUOTE_TEXT = "text"
        private const val QUOTE_BOOK_ID = "book_id"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createBooksTable = "CREATE TABLE $BOOKS_TABLE_NAME ($BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT, $BOOK_IS_READ INTEGER, $BOOK_TITLE TEXT, $BOOK_AUTHOR TEXT, $BOOK_PAGES TEXT, $BOOK_GENRE TEXT, $BOOK_RATING TEXT, $BOOK_READING_TIME TEXT, $BOOK_IDEA TEXT, $BOOK_THEME TEXT)"
        db?.execSQL(createBooksTable)
        val createQuotesTable = "CREATE TABLE $QUOTES_TABLE_NAME ($QUOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, $QUOTE_TEXT TEXT, $QUOTE_BOOK_ID INTEGER, FOREIGN KEY($QUOTE_BOOK_ID) REFERENCES $BOOKS_TABLE_NAME($BOOK_ID))"
        db?.execSQL(createQuotesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableBooks = "DROP TABLE IF EXISTS $BOOKS_TABLE_NAME"
        db?.execSQL(dropTableBooks)
        val dropTableQuotes = "DROP TABLE IF EXISTS $QUOTES_TABLE_NAME"
        db?.execSQL(dropTableQuotes)
        onCreate(db)
    }

    fun addBook(book: Book): Long {
        val values = ContentValues().apply {
            put(BOOK_TITLE, book.title)
            put(BOOK_AUTHOR, book.author)
            if (!book.isRead){
                put(BOOK_IS_READ, 0)
            }
            if (book.isRead){
                put(BOOK_IS_READ, 1)
            }
        }
        val db = writableDatabase
        val newRowId = db.insert(BOOKS_TABLE_NAME, null, values)
        db.close()
        return newRowId
    }
    fun updateBook(book:Book): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(BOOK_TITLE, book.title)
            put(BOOK_AUTHOR, book.author)
            if (!book.isRead){
                put(BOOK_IS_READ, 0)
            }
            if (book.isRead){
                put(BOOK_IS_READ, 1)
            }
            put(BOOK_PAGES, book.pages)
            put(BOOK_GENRE, book.genre)
            put(BOOK_READING_TIME, book.readingTime)
            put(BOOK_RATING, book.personalRating)
            put(BOOK_IDEA, book.favoriteIdea)
            put(BOOK_THEME, book.theme)
        }
        val rowsUpdated = db.update(BOOKS_TABLE_NAME, values, "$BOOK_ID=?", arrayOf(book.id.toString()))
        db.close()
        return rowsUpdated > 0
    }
    fun getAllBooks(): MutableList<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val cursor = db.query(BOOKS_TABLE_NAME, arrayOf(BOOK_ID, BOOK_IS_READ, BOOK_TITLE, BOOK_AUTHOR, BOOK_PAGES, BOOK_GENRE, BOOK_RATING, BOOK_READING_TIME, BOOK_IDEA, BOOK_THEME), null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BOOK_ID))
                val title = getString(getColumnIndexOrThrow(BOOK_TITLE))
                val author = getString(getColumnIndexOrThrow(BOOK_AUTHOR))
                val isReadInt = getInt(getColumnIndexOrThrow(BOOK_IS_READ))
                var isReadBool = false
                when (isReadInt){
                    1 ->isReadBool = true
                    0 ->isReadBool = false
                }
                val pages = getString(getColumnIndexOrThrow(BOOK_PAGES))
                val genre = getString(getColumnIndexOrThrow(BOOK_GENRE))
                val readingTime = getString(getColumnIndexOrThrow(BOOK_READING_TIME))
                val personalRating = getString(getColumnIndexOrThrow(BOOK_RATING))
                val favoriteIdea = getString(getColumnIndexOrThrow(BOOK_IDEA))
                val mainTheme = getString(getColumnIndexOrThrow(BOOK_THEME))
                books.add(Book(id, title, author, isReadBool, pages, genre, readingTime, personalRating, favoriteIdea, mainTheme))
            }
        }
        cursor.close()
        db.close()
        return books
    }
    fun deleteBook(book:Book): Int {
        val db = this.writableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(book.id.toString())
        val deletedRows = db.delete("BooksTable", selection, selectionArgs)
        db.close()
        return deletedRows
    }

    fun addQuote(bookId: Int, quote: Quote): Long {
        val values = ContentValues().apply {
            put(QUOTE_TEXT, quote.text)
            put(QUOTE_BOOK_ID, bookId)
        }
        val db = writableDatabase
        val newRowId = db.insert(QUOTES_TABLE_NAME, null, values)
        db.close()
        return newRowId
    }
    fun updateQuote(quote:Quote, newQuoteText: String): Boolean {
        val values = ContentValues().apply {
            put("text", newQuoteText)
        }
        val db = this.writableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(quote.id.toString())
        val count = db.update("QuotesTable", values, selection, selectionArgs)
        return count == 1
    }
    fun getQuotesForBook(bookId: Int): MutableList<Quote> {
        val quotes = mutableListOf<Quote>()
        val db = readableDatabase
        val cursor = db.query(QUOTES_TABLE_NAME, arrayOf(QUOTE_ID, QUOTE_TEXT, QUOTE_BOOK_ID), "$QUOTE_BOOK_ID=?", arrayOf(bookId.toString()), null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val quoteID = getInt(getColumnIndexOrThrow(QUOTE_ID))
                val quoteText = getString(getColumnIndexOrThrow(QUOTE_TEXT))
                val quoteBookID = getInt(getColumnIndexOrThrow(QUOTE_BOOK_ID))
                quotes.add(Quote(quoteID,quoteText,quoteBookID))
            }
        }
        cursor.close()
        db.close()
        return quotes
    }
    fun deleteQuote(quote: Quote): Boolean {
        val db = this.writableDatabase
        val result = db.delete(QUOTES_TABLE_NAME, "$QUOTE_ID=?", arrayOf(quote.id.toString()))
        db.close()
        return result != -1
    }
}