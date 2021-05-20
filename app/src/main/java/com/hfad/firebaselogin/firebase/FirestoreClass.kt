package com.hfad.firebaselogin.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hfad.firebaselogin.activities.LoginActivity
import com.hfad.firebaselogin.activities.SignUpActivity
import com.hfad.firebaselogin.models.User
import com.hfad.firebaselogin.utils.Constants

class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance() //Instance of the firebase store

    fun registerUser(activity:SignUpActivity, userInfo: User){
            mFireStore.collection(Constants.USERS)
                    //Pass in the current user, the one who is logged in
                    .document(getCurrentUserID())
                    //Set the user info and merge it
                    .set(userInfo, SetOptions.merge())
                    //Did it work? Then execute this code
                    .addOnSuccessListener { activity.userRegistredSuccess()}
    }

    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun signInUser(activity: LoginActivity){
        mFireStore.collection(Constants.USERS)
                //Pass in the current user, the one who is logged in
                .document(getCurrentUserID())
                //get the current user
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

}