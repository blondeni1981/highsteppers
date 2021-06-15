package com.hfad.firebaselogin.activities

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.hfad.firebaselogin.FireWalkAdapter
import com.hfad.firebaselogin.R
import com.hfad.firebaselogin.utils.Constants

class EditWalkActivity : BaseActivity() {
    val a= FirebaseAuth.getInstance().currentUser!!.uid
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference =db.collection(Constants.USERS)

    var fAdapter: FireWalkAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_walk_activity)
    }
//    fun setUpRecyclerView(){
//
//        val query: Query = db.collection(Constants.USERS)
//        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Walk> =
//                FirestoreRecyclerOptions.Builder<Walk>()
//                .set(query, Walk::class.java)
//                .build()
//
//        fAdapter = FireWalkAdapter(firestoreRecyclerOptions)
//
//
//
//    }
}