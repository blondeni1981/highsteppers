package com.hfad.firebaselogin.activities

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hfad.firebaselogin.R
import kotlinx.android.synthetic.main.activity_step_counter.*

class StepCounterActivity : AppCompatActivity(), SensorEventListener {

    //declare vars
    var running = false
    var sensorManager:SensorManager? = null
    var stepCount = -1
    var savedSteps = 0
    var stepGoal = 50

    // On create
    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Display Header
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Step Counter")

        // Service Test
        ForegroundService.startService(this, "Foreground Service is running...")

        // Step Goal
        stepGoalOutput.text = "" + stepGoal

        // Saved Steps
        savedStepOutput.text = "" + GlobalClass.Companion.globalSavedSteps

        saveButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("SAVED_STEPS", GlobalClass.Companion.globalCurrentSteps)
            startActivity(intent)
        }

    }

    // On Resume
    override fun onResume() {
        super.onResume()

        // OnResume vars
        running = true
        var stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // detect step sensor
        if (stepSensor == null){
            Toast.makeText(this,"No Step Counter Detected", Toast.LENGTH_SHORT).show()
        }
        else{
            sensorManager?.registerListener(this,stepSensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
        }
    }

    // On Pause -- No functionality
    override fun onPause() {
        super.onPause()
        //reset
        GlobalClass.Companion.globalCurrentSteps--
        //running = false
        sensorManager?.unregisterListener(this)
    }

    // OnAccuracyChanged -- No functionality
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    // OnSensorChanged detects a new step each time
    override fun onSensorChanged(event: SensorEvent?) {

        if (running){
            if (event != null) {

                //TODO rework the sensor logic so that the steps start at 0, but the saved step is not ahead of the display.

                // increment the stepCount
                //stepCount++

                GlobalClass.Companion.globalCurrentSteps++

                // display the steps
                stepOutput.text = "" + GlobalClass.Companion.globalCurrentSteps

                // check goal
                if (GlobalClass.Companion.globalCurrentSteps >= stepGoal)
                {
                    stepGoalOutput.text = "Complete!"
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------
    // Custom Functions
    //----------------------------------------------------------------------------------------------------------------------------------

    // Back Button Function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    // Reset Button
    fun resetSteps(view: View) {

        // Stop test
        ForegroundService.stopService(this)

        // Reset the text and stepCounter var to 0
        GlobalClass.Companion.globalCurrentSteps = 0
        stepOutput.text = "" + GlobalClass.Companion.globalCurrentSteps


        // Reset the text and stepGoal var to 0
        stepGoalOutput.text = "" + stepGoal
    }

    // Save Button
    fun saveSteps(view: View) {

        // set the save to the current stepCount
        savedSteps = GlobalClass.Companion.globalCurrentSteps
        //savedStepOutput.text = "" + savedSteps

        GlobalClass.Companion.globalSavedSteps = savedSteps

        savedStepOutput.text = "" + GlobalClass.Companion.globalSavedSteps

    }

    //Mock Step button
    fun addStep(view: View) {
        GlobalClass.Companion.globalCurrentSteps++

        // display the steps
        stepOutput.text = "" + GlobalClass.Companion.globalCurrentSteps

        // check goal
        if (GlobalClass.Companion.globalCurrentSteps >= stepGoal)
        {
            stepGoalOutput.text = "Complete!"
        }
    }

}