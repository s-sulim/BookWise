package com.example.bookwise.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.bookwise.R
import kotlinx.coroutines.*

class SplashScreenActivity : AppCompatActivity() {
    private var isGoalSaved: Boolean = false
    private lateinit var ivLogo: ImageView
    private lateinit var tvVersion: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        ivLogo = findViewById(R.id.iv_logo)
        tvVersion = findViewById(R.id.tvVersion)
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            tvVersion.text = "Version: $version"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        tvVersion.alpha = 0f
        GlobalScope.launch {
            tvVersion.animate().setDuration(500).alpha(1f)
            Thread.sleep(1500)
            tvVersion.animate().setDuration(500).alpha(0f)
        }
        checkGoalPresence()
    }
    private fun checkGoalPresence(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedGoal:String? = sharedPreferences.getString("GOAL", null)
        isGoalSaved = !savedGoal.isNullOrEmpty()
        val activity:Activity
        if (isGoalSaved) {
            activity = HomeActivity()
            animateAndStartActivity(activity)
        }
        else if(!isGoalSaved){
            activity = FirstStartActivity()
            animateAndStartActivity(activity)
        }
    }
    private fun animateAndStartActivity(activity:Activity){
        ivLogo.alpha = 0f
        ivLogo.animate().setDuration(2000).alpha(1f).withEndAction {
            val intent = Intent(this, activity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}