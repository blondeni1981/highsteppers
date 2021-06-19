package com.hfad.firebaselogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hfad.firebaselogin.activities.GlobalClass
import com.hfad.firebaselogin.activities.MainActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Display Header
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("User Settings")
    }

    // Back Button Function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun saveSettings(view: View) {

        val goal = savedStepsInput.toString().toInt()
        //test
        GlobalClass.Companion.globalSavedSteps = goal
        //savedStepOutput.text = "" + GlobalClass.Companion.globalSavedSteps

    }


}