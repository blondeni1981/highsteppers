package com.hfad.firebaselogin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.hfad.firebaselogin.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN

        )
        //Function gives the splash screen activity for 3.5 seconds, passing the activity to Intro
        Handler().postDelayed({
            startActivity(Intent(this, IntroActivity::class.java))
            //finish does not let a user go back to the previous screen
            finish()
        },3500)
    }
}