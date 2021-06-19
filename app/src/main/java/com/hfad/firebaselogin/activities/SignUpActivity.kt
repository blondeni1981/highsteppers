package com.hfad.firebaselogin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hfad.firebaselogin.R
import com.hfad.firebaselogin.firebase.FirestoreClass
import com.hfad.firebaselogin.models.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnSignUpCreate.setOnClickListener {
            registerUser()
        }

        // Display Header
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Sign Up Page")
    }

    // Back Button Function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun userRegistredSuccess() {
        Toast.makeText(
            this,
            "you have successfully registered",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun registerUser() {
        val name: String = textSignUp.text.toString().trim { it <= ' '}
        val email: String = textSignIn.text.toString().trim {it <= ' '}
        val password: String = textCreatePassword.text.toString().trim {it <= ' '}

        if(validateForm(name,email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                hideProgressDialog()
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email!!
                    val user = User(firebaseUser.uid, name, registeredEmail)
                    FirestoreClass().registerUser(this, user)

                    //added user name as global
                    GlobalClass.Companion.globalUser = name

                    //all done
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        task.exception!!.message, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }



    private fun validateForm(name: String,
                             email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please Enter a name")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please Enter an email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please Enter an password")
                false
            }
            else -> {
                true
            }
        }
    }
}

