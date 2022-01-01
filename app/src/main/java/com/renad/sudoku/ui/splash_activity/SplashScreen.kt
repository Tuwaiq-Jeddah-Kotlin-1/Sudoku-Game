package com.renad.sudoku.ui.splash_activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.renad.sudoku.R
import com.renad.sudoku.ui.main_activity.MainActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splashLogo.alpha = 0f
        splashLogo.animate().setDuration(3000).alpha(1f).withEndAction {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        val sharedPref = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)

        //check the dark mood user option
        if (sharedPref.getBoolean("DARK_MOOD",true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        //check localization user option
        if (sharedPref.getString("LOCALE","") == "ar"){
            setLocale(this,"ar")
        }else{
            setLocale(this,"en")
        }

    }

    private fun setLocale(activity: Activity, lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        startActivity(Intent(activity, MainActivity::class.java))
        activity.finish();
    }
}