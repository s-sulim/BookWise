package com.example.bookwise.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import com.example.bookwise.fragments.AddBookFragment
import com.example.bookwise.classes.Book
import com.example.bookwise.classes.MyBookAdapter
import com.example.bookwise.R
import com.example.bookwise.classes.BookDatabaseHelper

class HomeActivity : AppCompatActivity() {
    private lateinit var btnAddNewBook: Button
    private lateinit var tvNoBooks:TextView
    private lateinit var recyclerView:RecyclerView
    private lateinit var tvMyLibrary: TextView
    private lateinit var tvCurrentGoal:TextView
    private lateinit var myAdapter: MyBookAdapter
    private var booksAmount:Int = 0
    private var savedGoal:String? = null
    private var MyDatabaseHelper = BookDatabaseHelper(this)
    private var books = mutableListOf<Book>(                                                     //creating a list of books (can be empty)
      /* Book("1984","Джордж Орвелл", true),
        Book("The Subtle Art Of Not Giving A Fuck","Mark Manson", true),
        Book("Прекрасний новий світ","Олдос Гакслі", true),
        Book("Кульбабове вино","Рей Бредбері", true),
        Book("Мистецтво любові","Еріх Фромм", true),
        Book("Три зозулі з поклоном","Григір Тютюнник", true),
        Book("Ikigai","H.Garcia and F. Miralles", true),
        Book("Man's search for meaning","Viktor Frankl", true),
        Book("12 Rules For Life","Jordan Peterson", false)*/
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.i("MYTAG", "HomeActivity: OnCreate")

        tvCurrentGoal = findViewById(R.id.tvBookTitle)
        books = MyDatabaseHelper.getAllBooks()
        sortLibrary()
        loadGoal()                                                                                  //loading saved preferences

        val bookWiseToolBar = findViewById<Toolbar>(R.id.bookWiseToolBar)                           //setting the toolBar as the actionBar
        setSupportActionBar(bookWiseToolBar)

        recyclerView = findViewById(R.id.RecyclerView)
        btnAddNewBook = findViewById(R.id.btnAddBook)
        tvNoBooks = findViewById(R.id.tvNoBooks)
        tvMyLibrary = findViewById(R.id.tvQuotes)
        booksAmount = books.size                                                                 //setting the booksAmount to the list's size

        initialiseInterface()

        tvCurrentGoal.setOnClickListener{
            val intent= Intent(this, MyProgressActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        tvMyLibrary.setOnClickListener{
            Toast.makeText(this,
                "You have saved $booksAmount books to your library!",
                Toast.LENGTH_LONG).show()
        }
        btnAddNewBook.setOnClickListener {
            var dialog = AddBookFragment()
            dialog.show(supportFragmentManager,"customFragment")
        }
        recyclerView.layoutManager = LinearLayoutManager(this)                               //setting the LayoutManager for recyclerView
        myAdapter = MyBookAdapter(books) { selectedBook: Book ->                        //assigning our adapter as an adapter for rv
            listItemClicked(selectedBook)
        }
        recyclerView.adapter=myAdapter
    }



    fun setBookInfo(author: String, name: String, isRead:Boolean){                                                  //getting new book info from the dialog fr.
        tvNoBooks.isVisible = false
        MyDatabaseHelper.addBook(Book(name, author, isRead))
        books = MyDatabaseHelper.getAllBooks()
        booksAmount = books.size
        tvMyLibrary.text = "My library: $booksAmount"
        sortLibrary()
        myAdapter = MyBookAdapter(books) { selectedBook: Book ->                        //assigning our adapter as an adapter for rv
            listItemClicked(selectedBook)
        }
        recyclerView.adapter= myAdapter                                                                       //assigning our adapter as an adapter for rv
    }
    private fun listItemClicked(book: Book){
        books = MyDatabaseHelper.getAllBooks()
        var bookIndex: Int = 0
        for (booksItem in books){
            if (booksItem.title ==book.title){
                bookIndex = books.indexOf(booksItem)
            }
        }

        val i = Intent(this, BookDetailsActivity::class.java)
        i.putExtra("CURRENT BOOK INDEX", bookIndex)
        startActivity(i)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    private fun initialiseInterface(){
        tvMyLibrary.text = "My library: $booksAmount"
        if (booksAmount !=0)
            tvNoBooks.isVisible = false
    }
    private fun loadGoal(){                                                                         // loading the shared prefs
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        savedGoal = sharedPreferences.getString("GOAL", null)
        tvCurrentGoal.text = "Current goal: $savedGoal"
    }
    override fun onRestart() {
        super.onRestart()
        Log.i("MYTAG", "HomeActivity: OnRestart")
        loadGoal()
        updateInterface()
    }
    private fun sortLibrary() {
        books = MyDatabaseHelper.getAllBooks()
        if(!books.isNullOrEmpty()){
            val comparator= compareByDescending<Book> {it.isRead}
            books.sortWith(comparator)
        }
    }
    private fun updateInterface(){
        recyclerView.layoutManager = LinearLayoutManager(this)                               //setting the LayoutManager for recyclerView
        myAdapter = MyBookAdapter(books) { selectedBook: Book ->                        //assigning our adapter as an adapter for rv
            listItemClicked(selectedBook)
        }
        recyclerView.adapter=myAdapter
        sortLibrary()
        tvNoBooks.isVisible = MyDatabaseHelper.getAllBooks().isEmpty()
    }
    override fun onPause() {
        super.onPause()
        Log.i("MYTAG", "HomeActivity: OnPause")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MYTAG", "HomeActivity: OnResume")
        loadGoal()
        updateInterface()
    }
    override fun onStop() {
        super.onStop()
        Log.i("MYTAG", "HomeActivity: OnStop")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("MYTAG", "HomeActivity: OnDestroy")
    }


}