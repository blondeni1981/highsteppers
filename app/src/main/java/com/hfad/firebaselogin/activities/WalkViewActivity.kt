package com.hfad.firebaselogin.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hfad.firebaselogin.R
import com.hfad.firebaselogin.firebase.FirestoreClass
import com.hfad.firebaselogin.models.Walk
import com.hfad.firebaselogin.utils.Constants
import kotlinx.android.synthetic.main.activity_walk_view.*

class WalkViewActivity : AppCompatActivity() {

    private val db = Firebase.firestore //Instantiate an instance of a FireStore
    private var CollectionReference = db.collection(Constants.USERS) //Points to the users collection
    private val loggedInUser = FirebaseAuth.getInstance().currentUser!!.uid //get the currently logged in userID
    private var userWalks=FirebaseAuth.getInstance().currentUser!!.email+" Walks"
    //var bundle: Bundle? = intent.extras





    override fun onCreate(savedInstanceState: Bundle?) {
        val mybundle = intent.getBundleExtra("DISPLAY_WALK")
        var myObject= mybundle?.getParcelable<Walk>("key")
        var bundle: Bundle? = intent.extras
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_view)
//        bundle?. apply { val thisWalk:Walk? =getParcelable("DISPLAY_WALK")
//        if(thisWalk != null){
//            Log.d("Walk Details", "$thisWalk")
            tv_display_name.text = myObject!!.WalkName
            tv_display_location.text = myObject.WalkLocation
            tvDisplayEditLocation.hint = myObject.WalkName

        fab_edit_name.setOnClickListener {
            var editName=tvDisplayEditLocation.text
            CollectionReference.document(loggedInUser)
                .collection(userWalks)
                .document((myObject.WalkID!!)).update("walkName",editName.toString())
                .addOnSuccessListener { success ->
                    Log.d("Update Worked", "Document updated $success")
                    hideKeyboard()
                    Toast.makeText(this,
                        "Walk updated to $editName",
                        Toast.LENGTH_LONG).show()
                        var mainIntent=Intent(this, MainActivity::class.java)
                        startActivity(mainIntent)

                }.addOnFailureListener { e ->
                    Log.e("No ID", "Error Writing Document", e)
                }


        }
        }
    //Hides Keyboard immediately, so the user can see the Toast, called above
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }










    }
