package com.example.bookwise.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.bookwise.R
import com.example.bookwise.classes.Book
import com.example.bookwise.classes.BookDatabaseHelper
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class MyProgressActivity : AppCompatActivity() {
    private var MyDatabaseHelper = BookDatabaseHelper(this)
    private var savedGoal:String? = null
    private var newGoal: String? = null
    private var booksAmount:Int = 0
    private lateinit var etCurrentGoal: TextView
    private lateinit var tvGoal: TextView
    private lateinit var tvPercentage:TextView
    private lateinit var tvChallenge:TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var calendarView : CalendarView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_progress)

        progressBar = findViewById(R.id.progressBar)
        tvPercentage = findViewById(R.id.tvPercentage)
        tvGoal = findViewById(R.id.tvGoal)
        etCurrentGoal = findViewById(R.id.etCurrentGoal)
        tvChallenge = findViewById(R.id.tvChallenge)
        calendarView = findViewById(R.id.calendarView)
        etCurrentGoal.isVisible = false

        loadGoalAndProgress()
        val currentChallenge = savedGoal?.toInt()?.div(12)
        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()
        val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
        calendarView.setDate(today.timeInMillis)
        tvChallenge.text = "\uD83C\uDFC6 $currentMonth challenge: $currentChallenge books!"
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Do something with the selected date
        }

        tvGoal.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Changing the goal")
            builder.setMessage("You can change your goal only once a month. Continue?")
            builder.setPositiveButton("Yes") { _, _ ->
               checkTimestamp()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

    }
    private fun loadGoalAndProgress(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val books = MyDatabaseHelper.getAllBooks()
        for (book in books){
            if (book.isRead)
                booksAmount++
        }
        savedGoal = sharedPreferences.getString("GOAL", null)
        tvGoal.text = "$savedGoal books a year"
        etCurrentGoal.text = savedGoal
        progressBar.progress = booksAmount
        var goalInt = savedGoal?.toInt()
        if (goalInt != null) {
            progressBar.max = goalInt
        }
        tvPercentage.text = (booksAmount*100/ goalInt!!).toString() + "% ($booksAmount out of $savedGoal books read)"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        newGoal = etCurrentGoal.text.toString()
        saveGoal(newGoal)
    }
    private fun saveGoal(newGoal:String?) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("GOAL", newGoal)
            putLong("goal_last_changed_timestamp", System.currentTimeMillis())
            apply()
        }
    }
    fun checkTimestamp(){
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val lastChangedTimestamp = sharedPreferences.getLong("goal_last_changed_timestamp", 0)
        val currentTime: Long = System.currentTimeMillis()
        val oneMonthInMillis:Long = 2592000000 // 1 month in milliseconds
        if (currentTime - lastChangedTimestamp < oneMonthInMillis) {
            Toast.makeText(this,"Sorry, a month still hasn't passed since the last goal change.",Toast.LENGTH_SHORT).show()
        } else {
            tvGoal.isVisible = false
            etCurrentGoal.isVisible = true
            newGoal = etCurrentGoal.text.toString()
            saveGoal(newGoal)
        }
    }
}