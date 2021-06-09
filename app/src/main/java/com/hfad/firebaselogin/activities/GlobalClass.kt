package com.hfad.firebaselogin.activities

import android.app.Application

class GlobalClass: Application() {
    object Companion {
        var globalSavedSteps = 0
        var globalCurrentSteps = -1
    }
}