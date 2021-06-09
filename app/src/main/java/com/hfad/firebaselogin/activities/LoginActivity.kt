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
        //Create an object of FirebaseAuth Class
        //https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth
        auth = FirebaseAuth.getInstance()

        //Removes Top Menu
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        //Creating a listener for a button, execute this function when user clicks
        btnSignIn.setOnClickListener {
            registerUser()
        }
    }

    fun userLoginSuccess(user:User){
        hideProgressDialog()
        //move this activity and the screen to MainActivity
        startActivity(Intent(this, StepCounterActivity::class.java))
        //finish will prevent the user from being able to navigating back to this page
        finish()
       // Toast.makeText(this,"You have successfully logged in", Toast.LENGTH_LONG)
    }

    private fun registerUser(){
        //Get the text from the TextValues that the user has inputted, by their ID's and convert it
        //to a variable
        //The trim function eliminates any spaces/white space after their last character
        val email: String = textUsername.text.toString().trim {it <= ' '}
        val password: String = textPassword.text.toString().trim {it <= ' '}

        //If the user has typed something in both fields, true
        if(validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            //Signing
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        //Creates a message in the Logcat that you can find, breadcrumbs
                        Log.d("Sign in", "signInWithEmail:success")
                        // Successful login, use call method in Firestore and pass the activity
                        FirestoreClass().signInUser(this)
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }

        }
    }
    //TextUtils.isEmpty returns a Boolean depending on whether a string is null/0-length
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