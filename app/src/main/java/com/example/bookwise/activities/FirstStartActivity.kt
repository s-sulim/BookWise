package com.example.bookwise.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.bookwise.R

class FirstStartActivity : AppCompatActivity() {
    private lateinit var enteredText: String                                                        //value of the entered goal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_start)

        val submitButton = findViewById<Button>(R.id.btn_OK)
        val nextButton = findViewById<Button>(R.id.btn_SwitchActivity)
        val inputField = findViewById<EditText>(R.id.etGoal)

        nextButton.alpha = 0f                                                                       // set primary alpha to the two buttons
        submitButton.alpha = 1f

        submitButton.setOnClickListener {
            enteredText = inputField.text.toString()
            if (enteredText == "") {
                Toast.makeText(
                    this@FirstStartActivity,
                    "Don't forget to enter your goal!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                    submitButton.animate().setDuration(100).alpha(0f).withEndAction{
                        submitButton.visibility = View.INVISIBLE
                    }
                    nextButton.animate().setDuration(1200).alpha(1f)                           //animate the disappearance of nextButton
                    nextButton.visibility = View.VISIBLE
                    inputField.visibility = View.INVISIBLE
                }
            }

            nextButton.setOnClickListener() {
                val i = Intent(this, HomeActivity::class.java)
                saveGoal()
                startActivity(i)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

    private fun saveGoal() {
        val sharedPreferences:SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE) //save shared prefs
        val editor = sharedPreferences.edit()
        editor.apply{
            putString("GOAL", enteredText)
            putLong("goal_last_changed_timestamp", System.currentTimeMillis())
            apply()
        }
        Toast.makeText(this, "Goal set!", Toast.LENGTH_LONG).show()
    }
}

