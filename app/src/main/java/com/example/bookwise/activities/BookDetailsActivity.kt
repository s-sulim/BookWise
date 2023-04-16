package com.example.bookwise.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookwise.R
import com.example.bookwise.classes.*
import com.example.bookwise.fragments.AddIdeaFragment
import com.example.bookwise.fragments.AddQuoteFragment
import com.example.bookwise.fragments.AddThemeFragment
import com.example.bookwise.fragments.EditQuoteFragment


class BookDetailsActivity : AppCompatActivity() {
    private lateinit var etBookTitle:EditText
    private lateinit var etBookAuthor:EditText
    private lateinit var etBookPages:EditText
    private lateinit var etBookReadingTime:EditText
    private lateinit var etBookRating:EditText
    private lateinit var etBookGenre:EditText
    private lateinit var cbRead: CheckBox
    private lateinit var cbUnread: CheckBox
    private lateinit var btnSaveChanges: Button
    private lateinit var btnDeleteBook: Button
    private lateinit var tvIdea:TextView
    private lateinit var tvTheme:TextView
    private lateinit var tvThemeText:TextView
    private lateinit var tvIdeaText:TextView
    private lateinit var tvMyQuotes:TextView
    private lateinit var tvBookTitle: TextView
    private lateinit var tvNoQuotes: TextView
    private lateinit var rvQuotes: RecyclerView
    private lateinit var currentBook: Book
    private var currentBookIndex:Int = 0
    private var MyBookDatabaseHelper = BookDatabaseHelper(this)
    private lateinit var books: MutableList<Book>
    private lateinit var quotes: MutableList<Quote>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)
        /*           Getting Book Info           */

        books = MyBookDatabaseHelper.getAllBooks()
        currentBookIndex = intent.getIntExtra("CURRENT BOOK INDEX", 0)
        currentBook = books[currentBookIndex]
        quotes = MyBookDatabaseHelper.getQuotesForBook(currentBookIndex)
        //loadBookQuotes()
        //region Initializing views
        /*          Initializing EditTexts       */
        etBookTitle = findViewById(R.id.etBookTitle)
        etBookAuthor = findViewById(R.id.etBookAuthor)
        etBookPages = findViewById(R.id.etBookPages)
        etBookGenre = findViewById(R.id.etBookGenre)
        etBookReadingTime = findViewById(R.id.etBookReadingTime)
        etBookRating = findViewById(R.id.etBookRating)

        /*        Initializing CheckBoxes       */
        cbRead = findViewById(R.id.cbRead)
        cbUnread = findViewById(R.id.cbUnread)

        /*        Initializing Buttons          */
        btnDeleteBook = findViewById(R.id.btnDeleteBook)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)

        /*        Initializing TextViews        */
        tvBookTitle = findViewById(R.id.tvBookTitle)
        tvIdea = findViewById(R.id.tvIdea)
        tvTheme = findViewById(R.id.tvTheme)
        tvMyQuotes = findViewById(R.id.tvMyQuotes)
        tvThemeText = findViewById(R.id.tvThemeText)
        tvIdeaText = findViewById(R.id.tvIdeaText)
        tvNoQuotes = findViewById(R.id.tvNoQuotes)

        /*        Initializing RecyclerView      */
        rvQuotes = findViewById(R.id.rvQuotes)
        //endregion
        initializeInterface()
        //region click listeners

        btnSaveChanges.setOnClickListener{
            currentBook.isRead = cbRead.isChecked
            currentBook.title = etBookTitle.text.toString()
            currentBook.author = etBookAuthor.text.toString()
            if (tvIdeaText.text.toString() != "..."){
                currentBook.favoriteIdea= tvIdeaText.text.toString()
            }
            if (tvThemeText.text.toString()!= "..."){
                currentBook.theme= tvThemeText.text.toString()
            }
            if (!etBookPages.text.toString().isNullOrEmpty()){
                currentBook.pages = etBookPages.text.toString()
            }
            if (!etBookReadingTime.text.toString().isNullOrEmpty()){
                currentBook.readingTime = etBookReadingTime.text.toString()
            }
            if (!etBookRating.text.toString().isNullOrEmpty()){
                currentBook.personalRating = etBookRating.text.toString()
            }
            if (!etBookGenre.text.toString().isNullOrEmpty()){
                currentBook.genre = etBookGenre.text.toString()
            }
           // saveBookQuotes()
            MyBookDatabaseHelper.updateBook(currentBook)
        }
        btnDeleteBook.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete «${currentBook.title}»")
            builder.setMessage("Are you sure you want to delete this book? This action cannot be undone.")
            builder.setPositiveButton("Yes") { _, _ ->
                val quotesToErase = MyBookDatabaseHelper.getQuotesForBook(currentBookIndex)
                MyBookDatabaseHelper.deleteBook(currentBook)
                for (quote in quotesToErase){
                    MyBookDatabaseHelper.deleteQuote(quote)
                }
                Toast.makeText(this,"Book deleted!", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        tvIdea.setOnClickListener{
            var dialog = AddIdeaFragment()
            val bundle = Bundle()
            bundle.putString("Current Book Idea", currentBook.favoriteIdea)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager,"customFragment")
        }
        tvTheme.setOnClickListener{
            var dialog = AddThemeFragment()
            val bundle = Bundle()
            bundle.putString("Current Book Theme", currentBook.theme)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager,"customFragment")
        }
        tvMyQuotes.setOnClickListener{
            var dialog = AddQuoteFragment()
            dialog.show(supportFragmentManager,"customFragment")
            if (quotes.isNotEmpty()){
                tvNoQuotes.isVisible = false
            }
        }
        rvQuotes.layoutManager = LinearLayoutManager(this)
        rvQuotes.adapter = MyQuotesAdapter(quotes) { selectedQuote: Quote ->                        //assigning our adapter as an adapter for rv
            listItemClicked(selectedQuote)
        }
        cbRead.setOnClickListener{
            cbUnread.isChecked= false
        }
        cbUnread.setOnClickListener{
            cbRead.isChecked= false
        }
        //endregion
    }

    private fun listItemClicked(selectedQuote: Quote) {
        var dialog = EditQuoteFragment()
        val bundle = Bundle()
        bundle.putString("Current Quote Text", selectedQuote.text)
        bundle.putInt("Current Quote ID", selectedQuote.id)
        bundle.putInt("Current Quote Book ID", selectedQuote.book_id)
        bundle.putInt("Current Book Index",currentBookIndex)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager,"customFragment")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        MyBookDatabaseHelper.updateBook(currentBook)
    }
    private fun initializeInterface(){
        val bookWiseToolBar = findViewById<Toolbar>(R.id.detailsToolbar)
        setSupportActionBar(bookWiseToolBar)

        tvBookTitle.text = "«${currentBook.title}»"
        etBookTitle.setText(currentBook.title)
        etBookAuthor.setText(currentBook.author)
        etBookPages.setText(currentBook.pages)
        etBookGenre.setText(currentBook.genre)
        etBookRating.setText(currentBook.personalRating)
        etBookReadingTime.setText(currentBook.readingTime)

        if(!currentBook.favoriteIdea.isNullOrEmpty()){
            tvIdeaText.text = currentBook.favoriteIdea
        }
        if(!currentBook.theme.isNullOrEmpty()){
            tvThemeText.text = currentBook.theme
        }
        if (currentBook.isRead){
            cbRead.isChecked = true
        }
        else{
            cbUnread.isChecked = true
        }
        if (quotes.isNotEmpty()){
            tvNoQuotes.isVisible = false
        }
    }
    fun setBookIdea(idea: String) {
        currentBook.favoriteIdea = idea
        tvIdeaText.text = currentBook.favoriteIdea
    }
    fun setBookTheme(theme: String) {
        currentBook.theme = theme
        tvThemeText.text = currentBook.theme
    }
    fun addBookQuote(quote: Quote) {
        MyBookDatabaseHelper.addQuote(currentBookIndex,quote)
        tvNoQuotes.isVisible = false
        updateQuotesRecyclerView()
    }
    fun editBookQuote(quote :Quote, newQuoteText:String) {
        MyBookDatabaseHelper.updateQuote(quote,newQuoteText)
        Toast.makeText(this,"Quote edited!", Toast.LENGTH_SHORT).show()
        updateQuotesRecyclerView()
    }
    fun deleteBookQuote(currentQuote: Quote) {
        MyBookDatabaseHelper.deleteQuote(currentQuote)
        updateQuotesRecyclerView()
        Toast.makeText(this,"Quote deleted!", Toast.LENGTH_SHORT).show()
    }
    private fun updateQuotesRecyclerView(){
        quotes = MyBookDatabaseHelper.getQuotesForBook(currentBookIndex)
        rvQuotes.layoutManager = LinearLayoutManager(this)
        rvQuotes.adapter = MyQuotesAdapter(quotes) { selectedQuote: Quote ->
            listItemClicked(selectedQuote)
        }
        if (quotes.isEmpty()){
            tvNoQuotes.isVisible = true
        }
    }
}