package com.hfad.firebaselogin.activities

import android.app.Application
import kotlin.math.roundToInt

class GlobalClass: Application() {
    object Companion {
        var globalSavedSteps = 0
        var globalCurrentSteps = -1
        var globalUser = ""
        var globalStepGoal = 50
        var globalWeight = 180
        var globalHeight = 72

        var globalCalories = 0.0


        val globalCalRounded:Double = (globalCalories * 100.0).roundToInt() / 100.0
    }
}