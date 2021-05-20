package com.hfad.firebaselogin.activities

import android.content.Intent
import android.os.Bundle
import com.hfad.firebaselogin.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        btnIntroSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        btnIntroSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}

