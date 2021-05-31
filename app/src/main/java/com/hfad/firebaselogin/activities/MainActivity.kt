package com.hfad.firebaselogin.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.api.Distribution
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.auth.User

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hfad.firebaselogin.R
import com.hfad.firebaselogin.firebase.FirestoreClass
import com.hfad.firebaselogin.models.Walk
import com.hfad.firebaselogin.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore
    var CollectionReference = db.collection(Constants.USERS)
    private lateinit var someHolder:String
    private lateinit var auth: FirebaseAuth
    val a = FirebaseAuth.getInstance().currentUser!!.uid
    var myWalks = DisplayWalks()
    private val adapter = WalksAdapter(myWalks) { partItem: Walk ->
        partItemClicked(
                partItem
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        val a = FirebaseAuth.getInstance().currentUser!!.uid
        auth = FirebaseAuth.getInstance()
        val query = db.collection(Constants.USERS).document(a)
        query.get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("Found It", "Document Data: ${document.data}")
                displayGet.text = document.getString("name")
            } else {
                Log.d("Didn't get it", "Failed to get document")
            }
        }
        buttonDatePicker.setOnClickListener {

            addWalk()
        }
        buttonDisplay.setOnClickListener {
            DisplayWalks()
        }
//        recyclerView.apply {
//            //DisplayWalks()
//            layoutManager = LinearLayoutManager(this@MainActivity)
//            adapter =  WalksAdapter(myWalks) {partItem:Walk ->
//                partItemClicked(
//                        partItem
//                )
//            }
//        }

    }

    private fun partItemClicked(partItem: Walk) {
        Toast.makeText(this, "Clicked, ${partItem}", Toast.LENGTH_LONG).show()

    }

    private fun addWalk() {
        //val a = FirebaseAuth.getInstance().currentUser!!.uid

        var newWalkTwo = Walk()
        newWalkTwo.WalkName = tvWalkName.text.toString()
        newWalkTwo.WalkLocation = tvWalkLocation.text.toString()
        newWalkTwo.WalkDistance = tvWalkDistance.text.toString()
        newWalkTwo.WalkID = "a"
        myWalks.add(0, newWalkTwo)
        adapter.notifyItemInserted(0)


       CollectionReference.document(a)
               .collection("Neil Walks").add(newWalkTwo)
               .addOnSuccessListener { b ->
                   if (b != null) {

                       //newWalkTwo.WalkID= b.id.toString()
                           someHolder=b.id
                       UpdateWalkId(someHolder)
                       Log.d("GettingId", someHolder)

                   }
               }.addOnFailureListener { e ->
                   Log.e("No ID", "Error Writing Document", e)
               }



    }
    private fun UpdateWalkId(id:String) {
        CollectionReference.document(a)
                .collection("Neil Walks")
                .document((id)).update("walkID", id)
                .addOnSuccessListener { success ->
                    Log.d("Update Worked", "Document updated")
                }.addOnFailureListener { e ->
                    Log.e("No ID", "Error Writing Document", e)
                }
    }

    private fun deleteWalk() {

    }

    private fun DisplayWalks(): ArrayList<Walk> {
        val myWalks = ArrayList<Walk>()
        val a = FirebaseAuth.getInstance().currentUser!!.uid
        CollectionReference.document(a)
                .collection("Neil Walks").get()
                .addOnSuccessListener { b ->
                    if (b != null) {

                        for (c in b.documents) {
                            var name = c.toObject<Walk>()
                            Log.d("Found It", "Document Data: $name and $b")
                            if (name != null) {
                                myWalks.add(name)
                            }
                        }

                        //displayWalk.text=document.documents.toString()

                    } else {
                        Log.d("Didn't get it", "Failed to get document")
                    }
                }

        return myWalks
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
