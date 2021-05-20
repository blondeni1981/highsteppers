package com.hfad.firebaselogin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hfad.firebaselogin.R
import com.hfad.firebaselogin.firebase.FirestoreClass
import com.hfad.firebaselogin.models.User
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        btnSignIn.setOnClickListener {
            registerUser()
        }
    }

    fun userLoginSuccess(user:User){
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
       // Toast.makeText(this,"You have successfully logged in", Toast.LENGTH_LONG)
    }

    private fun registerUser(){
        val email: String = textUsername.text.toString().trim {it <= ' '}
        val password: String = textPassword.text.toString().trim {it <= ' '}

        if(validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Sign in", "signInWithEmail:success")
//                        val firebaseUser : FirebaseUser =  task.result!!.user!!
//                        val registeredEmail = firebaseUser.email!!
//                        val user=User(firebaseUser.uid,email,registeredEmail)
                        val user = auth.currentUser
                        startActivity(Intent(this, MainActivity::class.java))
                        //FirestoreClass().signInUser(this)

                        //startActivity(Intent(this,MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }
//            Toast.makeText(
//                this@LoginActivity,
//                "Valid Characters Entered",
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }

    private fun validateForm(email: String, password: String):Boolean{
        return when {
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please Enter Email")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please Enter a Password")
                false
        }else -> {
            true

            }
        }
    }
}