package com.hfad.firebaselogin.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hfad.firebaselogin.activities.IntroActivity
import com.hfad.firebaselogin.activities.MainActivity
import com.hfad.firebaselogin.activities.SignUpActivity
import com.hfad.firebaselogin.models.User
import com.hfad.firebaselogin.utils.Constants

//FireStore class instantiates our Database object so we can define functions that save our data to
//Firestore Cloud
class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance() //Instance of the firebase store
    private val userWalks=FirebaseAuth.getInstance().currentUser!!.email+" Walks"

    //this function takes an activity and a user object (defined by our model)
    fun registerUser(activity:SignUpActivity, userInfo: User){
            //Points to a collection of users
            mFireStore.collection(Constants.USERS)
                    //Calling our method defined below, to get the current user
                    .document(getCurrentUserID())
                    //Set the user info and merge it, .merge() will not overwrite existing data
                    //https://firebase.google.com/docs/firestore/manage-data/add-data
                    //API CALL, Will create or update
                    .set(userInfo, SetOptions.merge())
                    //Did it work? Then execute this function, in SignUp Activity
                    .addOnSuccessListener { activity.userRegistredSuccess()}
    }

    fun getCurrentUserID(): String {
        //Get and return the currently logged in user, not null assertion operator !!,
        //throw an exception if null
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun signInUser(activity: IntroActivity){
        mFireStore.collection(Constants.USERS)
                //Pass in the current user, the one who is logged in
                .document(getCurrentUserID())
                //get the current user, API CALL
                .get()
                .addOnSuccessListener {  document -> //Give me the document and I will make an Object of it
                    val loggedInUser = document.toObject(User::class.java)//Make it an Object of User Class
                    if(loggedInUser != null)
                        activity.userLoginSuccess(loggedInUser)
                }.addOnFailureListener {
                    e->
                    Log.e("SignInUser", "Error Writing Document",e)
                }
                }

//    fun editWalkName(walk:Walk, edit: String){
//
//        val tempString = walk.WalkID
//        Log.d("Edit Caught","Changing ${walk.WalkName} to $edit")
//
//        mFireStore.collection(Constants.USERS)
//                .document(getCurrentUserID())
//                .collection(userWalks!!)
//                .document((tempString)).update("walkName", edit)
//                .addOnSuccessListener { success ->
//                    Log.d("EditName Worked", "Document updated $success")
//                }.addOnFailureListener { e ->
//                    Log.e("No ID", "Error Writing Document", e)
//                }
//    }

    fun getOneUser(activity: MainActivity){
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    val loggedInUser = document.toObject(User::class.java)
                    if(document != null){
                        Log.d("Found It", "Document Data: ${document.data}")

                    }else{
                        Log.d("Didn't get it","Failed to get document")
                    }

                }
                .addOnFailureListener{exception ->
                    Log.d("Failed", "get failed with a ",exception)
                }


    }

}