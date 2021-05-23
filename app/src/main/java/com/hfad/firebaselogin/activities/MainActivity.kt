package com.hfad.firebaselogin.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.auth.User

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hfad.firebaselogin.R
import com.hfad.firebaselogin.firebase.FirestoreClass
import com.hfad.firebaselogin.models.Walk
import com.hfad.firebaselogin.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.*

class MainActivity : AppCompatActivity() {
    val db=Firebase.firestore
   var CollectionReference = db.collection(Constants.USERS)
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val a = FirebaseAuth.getInstance().currentUser!!.uid
        auth= FirebaseAuth.getInstance()
        val query= db.collection(Constants.USERS).document(a)
        query.get().addOnSuccessListener { document ->
            if(document != null) {
                Log.d("Found It", "Document Data: ${document.data}")
                displayGet.text = document.getString("name")
            }else{
                Log.d("Didn't get it","Failed to get document")
            }
        }
        buttonDatePicker.setOnClickListener{
            addWalk()
        }
        buttonDisplay.setOnClickListener{
            DisplayWalks()
        }




    }
    private fun addWalk(){
        val a = FirebaseAuth.getInstance().currentUser!!.uid
        //val newWalk= Walk("Fall City Farms","6 miles", "Fall City")
        var newWalkTwo= Walk()
        newWalkTwo.WalkName = tvWalkName.text.toString()
        newWalkTwo.WalkLocation= tvWalkLocation.text.toString()
        newWalkTwo.WalkDistance = tvWalkDistance.text.toString()


        CollectionReference.document(a)
            .collection("Neil Walks").add(newWalkTwo)

    }
    private fun DisplayWalks(){
        val a = FirebaseAuth.getInstance().currentUser!!.uid
        CollectionReference.document(a)
            .collection("Neil Walks").get()
            .addOnSuccessListener { document ->
                if(document != null) {
                    Log.d("Found It", "Document Data: ${document.documents}")

                    displayWalk.text=document.documents.toString()

                }else{
                    Log.d("Didn't get it","Failed to get document")
                }
            }
    }

}

        //buttonDatePicker.setOnClickListener { view -> getTime(view) }

//    fun getTime(view: View) {
//        val rightNow = Calendar.getInstance()
//        Log.d("Sign in", "signInWithEmail:success")
//
//        val d1:Date=df.parse("10")
//
//        val year=rightNow.get(Calendar.YEAR)
//        val month=rightNow.get(Calendar.MONTH)
//        val day=rightNow.get(Calendar.DATE)
//
//        var militaryTime = rightNow.get(Calendar.HOUR_OF_DAY)
//        Toast.makeText(this, "day-$day, month is $month, year is $year,military time is:$militaryTime ", Toast.LENGTH_LONG).show()
//    }
