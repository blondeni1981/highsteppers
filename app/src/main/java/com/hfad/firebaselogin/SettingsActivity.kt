package com.hfad.firebaselogin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hfad.firebaselogin.activities.GlobalClass
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

        var setGoal = savedStepsInput.getText().toString().toInt()

        var setHeight = editTextNumber2.getText().toString().toInt()

        var setWeight = editTextNumber3.getText().toString().toInt()

//        val guessStep: Int
//        guessStep = try {
//            setGoal.toInt()
//        } catch (ex: NumberFormatException) {
//            //They didn't enter a number.  Pop up a toast or warn them in some other way
//            return
//        }


        //test
        if(setGoal is Int){
            GlobalClass.Companion.globalStepGoal = setGoal
        }
        else{
            GlobalClass.Companion.globalStepGoal = GlobalClass.Companion.globalStepGoal
        }

        if(setHeight is Int){
            GlobalClass.Companion.globalHeight = setHeight
        }
        else{
            GlobalClass.Companion.globalHeight = GlobalClass.Companion.globalHeight
        }

        if(setWeight is Int){
            GlobalClass.Companion.globalWeight = setWeight
        }
        else{
            GlobalClass.Companion.globalWeight = GlobalClass.Companion.globalWeight
        }

        Toast.makeText(
            this,
            "Settings Changed.",
            Toast.LENGTH_LONG
        ).show()

        //textView15.text = ""+GlobalClass.Companion.globalStepGoal+GlobalClass.Companion.globalHeight +GlobalClass.Companion.globalWeight+ "Calories Burned" + GlobalClass.Companion.globalCalories
    }

    fun resetDefaults(view: View) {
        GlobalClass.Companion.globalStepGoal = 50
        GlobalClass.Companion.globalHeight = 72
        GlobalClass.Companion.globalWeight = 180

        Toast.makeText(
            this,
            "Settings Reset to defaults.",
            Toast.LENGTH_LONG
        ).show()
    }


}