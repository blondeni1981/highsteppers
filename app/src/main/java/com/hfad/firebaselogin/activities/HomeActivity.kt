package com.hfad.firebaselogin.activities

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.hfad.firebaselogin.R

import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        bttn_view.setOnClickListener {

            startActivity(Intent(this, StepCounterActivity::class.java))
        }

        bttn_new.setOnClickListener {

            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}