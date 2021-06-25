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
import com.hfad.firebaselogin.ForegroundService
import com.hfad.firebaselogin.R
import kotlinx.android.synthetic.main.activity_step_counter.*

class StepCounterActivity : AppCompatActivity(), SensorEventListener {

    //declare vars
    var running = false
    var sensorManager:SensorManager? = null
    var savedSteps = 0
    var stepGoal = GlobalClass.Companion.globalStepGoal


    // On create
    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Display Header
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Step Counter")

        // Start the Foreground
        ForegroundService.startService(this, "Foreground Service is running...")

        // Step Goal
        stepGoalOutput.text = "" + GlobalClass.Companion.globalStepGoal

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

        // Start the Foreground
        ForegroundService.startService(this, "Foreground Service is running...")
    }

    // On Pause
    override fun onPause() {
        super.onPause()


        running = false
        sensorManager?.unregisterListener(this)
    }

    // OnAccuracyChanged -- No functionality
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    // OnSensorChanged detects a new step each time
    override fun onSensorChanged(event: SensorEvent?) {

        if (running){
            if (event != null) {


                if(GlobalClass.Companion.globalCurrentSteps == -1 || GlobalClass.Companion.globalCurrentSteps == 0){
                    stepOutput.text = "0"
                }
                else{
                    stepOutput.text = "" + GlobalClass.Companion.globalCurrentSteps
                }

                // check goal
                if (GlobalClass.Companion.globalCurrentSteps >= stepGoal)
                {
                    stepGoalOutput.text = "Done!"
                }

                //circular bar
                walkProgressBar.apply {
                    setProgressWithAnimation(GlobalClass.Companion.globalCurrentSteps.toFloat())
                    progressMax = stepGoal.toFloat()
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

        // Reset the text and stepCounter var to 0
        GlobalClass.Companion.globalCurrentSteps = -1

        // Stop Foreground
        //ForegroundService.stopService(this)
        val myService = Intent(this@StepCounterActivity, ForegroundService::class.java)
        stopService(myService)


        // Start Foreground
        val myService2 = Intent(this@StepCounterActivity, ForegroundService::class.java)
        startService(myService2)

        //stepOutput.text = "" + GlobalClass.Companion.globalCurrentSteps
        stepOutput.text = "" + 0

        // Reset the text and stepGoal var to 0
        stepGoalOutput.text = "" + GlobalClass.Companion.globalStepGoal

        // Reset Bar
        walkProgressBar.apply {
            setProgressWithAnimation(GlobalClass.Companion.globalCurrentSteps.toFloat())
            progressMax = stepGoal.toFloat()
        }
    }

    // Save Button
    fun saveSteps(view: View) {

        // Stop Foreground
        val myService = Intent(this@StepCounterActivity, ForegroundService::class.java)
        stopService(myService)

        // Save Steps
        savedSteps = GlobalClass.Companion.globalCurrentSteps
        GlobalClass.Companion.globalSavedSteps = savedSteps

        // Move to new page using intent
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("SAVED_STEPS", GlobalClass.Companion.globalCurrentSteps)
        startActivity(intent)

        // Calorie Count
        if(GlobalClass.Companion.globalHeight >= 72){
            GlobalClass.Companion.globalCalories = (GlobalClass.Companion.globalCurrentSteps * 0.047)
        }
        else{
            GlobalClass.Companion.globalCalories = (GlobalClass.Companion.globalCurrentSteps * 0.045)
        }

    }

    //Mock Step button
    fun addStep(view: View) {

        // Increment step
        GlobalClass.Companion.globalCurrentSteps++

        // display the steps
        stepOutput.text = "" + GlobalClass.Companion.globalCurrentSteps

        // check goal
        if (GlobalClass.Companion.globalCurrentSteps >= stepGoal)
        {
            stepGoalOutput.text = "Done!"
        }

        //update bar
        walkProgressBar.apply {
            setProgressWithAnimation(GlobalClass.Companion.globalCurrentSteps.toFloat())
            progressMax = stepGoal.toFloat()
        }
    }

}