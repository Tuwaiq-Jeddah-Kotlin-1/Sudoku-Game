package com.renad.sudoku.ui.main_activity

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.renad.sudoku.R
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)

        //check the dark mood user option
        if (sharedPref.getBoolean("DARK_MOOD", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        //check localization user option
        if (sharedPref.getString("LOCALE", "") == "ar") {
            setLocale(this, "ar")
        } else {
            setLocale(this, "en")
        }
    }

    fun setLocale(activity: Activity, lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

    }

    //notification
        private fun notification() {
        val periodicWorker = PeriodicWorkRequest.Builder(
            NotificationWorker::class.java, 20, TimeUnit.MINUTES//minimum 15 min
        ).build()//mad worker with periodic time repeat

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "periodicNotification",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorker
        )//unique work
    }
}