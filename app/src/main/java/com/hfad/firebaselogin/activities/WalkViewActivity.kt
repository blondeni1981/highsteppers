package com.hfad.firebaselogin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.hfad.firebaselogin.R
import com.hfad.firebaselogin.models.Walk
import kotlinx.android.synthetic.main.activity_walk_view.*

class WalkViewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        var bundle: Bundle? = intent.extras
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_view)
        bundle?.apply { val thisWalk:Walk? =getParcelable("DISPLAY_WALK")
        if(thisWalk != null){
            Log.d("Walk Details", "$thisWalk")
            tv_display_name.text = thisWalk.WalkName
            tv_display_location.text = thisWalk.WalkLocation
        }
        }




    }
}