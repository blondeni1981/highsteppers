package com.hfad.highsteppersprototype

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //declare buttons
        val stepCounterBtn = findViewById<Button>(R.id.stepCounterButton)
        val historyBtn = findViewById<Button>(R.id.historyButton)

        stepCounterBtn.setOnClickListener{
            val intent = Intent(this, StepCounterActivity::class.java)
            startActivity(intent)
        }

        historyBtn.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}