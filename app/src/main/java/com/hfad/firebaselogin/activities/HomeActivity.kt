package com.hfad.firebaselogin.activities

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.hfad.firebaselogin.R
import com.hfad.firebaselogin.SettingsActivity

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_step_counter.*


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Welcome the user
        welcomeMessage.text = "Welcome " + GlobalClass.Companion.globalUser

        //Removes Top Menu
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Start Step Counter
        bttn_view.setOnClickListener {

            startActivity(Intent(this, StepCounterActivity::class.java))
        }

        // Start History
        bttn_new.setOnClickListener {

            startActivity(Intent(this, WalkViewActivity::class.java))
        }

        // Start Settings
        bttn_settings.setOnClickListener {

            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Log Out
        bttn_logout.setOnClickListener {

            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }
    }
}