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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hfad.firebaselogin.R
import com.hfad.firebaselogin.models.Walk
import com.hfad.firebaselogin.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore //Instantiate an instance of a FireStore
    var CollectionReference = db.collection(Constants.USERS) //Points to the users collection
    private lateinit var someHolder: String
    private lateinit var auth: FirebaseAuth
    val loggedInUser = FirebaseAuth.getInstance().currentUser!!.uid //get the currently logged in userID
    //variable for the user SubCollection walks, making sure it links up with the currently logged in User
    var userWalks=FirebaseAuth.getInstance().currentUser!!.email+" Walks"
    //DisplayWalks goes up to FireStore to get the User's Walks, return it in an ArrayList
    var myWalks = DisplayWalks()
    //Create an instance of WalksAdapter, pass in ArrayList & a CallBack function, for itemClick Events
    //The WalksAdapter is the RecyclerView Adapter, its the engine that powers the RecyclerView
    private val adapter = WalksAdapter(myWalks) { partItem: Walk ->
        partItemClicked(
                partItem
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Pointing to RV in XML, then attaching the RV adapter from WalksAdapter class to it
        recyclerView.adapter = adapter
        //Choosing which layout manager we want, grid/linear
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        update(myWalks)
//      Below was a read from FireStore for testing purposes

//        val query = db.collection(Constants.USERS).document(a)
//        query.get().addOnSuccessListener { document ->
//            if (document != null) {
//                Log.d("Found It", "Document Data: ${document.data}")
//                displayGet.text = document.getString("name")
//            } else {
//                Log.d("Didn't get it", "Failed to get document")
//            }
//        }

        //Button that calls addWalk when clicked
        buttonDatePicker.setOnClickListener {
            addWalk()
        }
        //THIS IS FOR SWIPE TO DELETE
        //ItemTouchHelper(ITH) is a companion class to RecyclerView, must provide an ITH Callback
        //object which dictates the types of touches to respond to and what to do in each case
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false //Not moving or rearranging the list, returning false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //Calls method in WalksAdapter passing the view that  was swiped (to be deleted)
                var deleted = adapter.removeItem(viewHolder)
                Log.d("Walk Deleted", "$deleted")
                //The walk is now deleted from  local ArrayList, now it must be deleted from FireStore
                deleteWalk(deleted) //passing the deleted walkID to FireStore so it knows what to delete
            }

        }
        //Create a variable of type ITH, must pass in the SimpleCall back object we created above
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        //Attach the the ITH variable to our RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView)




    }
//https://www.journaldev.com/37763/android-intent-handling-between-activities-using-kotlin
    private fun partItemClicked(oneWalk: Walk) {
        Toast.makeText(this, "Clicked, $oneWalk", Toast.LENGTH_LONG).show()
        //This line below is interesting, but not wanted, makes a new list, but can't easily pass in the Intent
        //var filterID = myWalks.filter { s -> s.WalkID == partItem.WalkID }

        //Create a new Intent and pass it Display Activity, putExtra adds the walk
        val intent = Intent(this, WalkViewActivity::class.java)
        val bundle =Bundle()
        bundle.putParcelable("key", oneWalk)
        intent.putExtra("DISPLAY_WALK", bundle)
        startActivity(intent)
        //Parceable must be included in the model (Walk.kt) to allow the serialization of objects
        //to be passed from one activity to another in Intents

        intent.putExtra("DISPLAY_WALK", bundle)
        startActivity(intent)
    }


    private fun addWalk() { //Adds a new walk to FireStore and the local ArrayList

        val newWalkTwo = Walk()
        newWalkTwo.WalkName = tvWalkName.text.toString()
        newWalkTwo.WalkLocation = tvWalkLocation.text.toString()
        newWalkTwo.WalkDistance = tvWalkDistance.text.toString()
        //Arbitrary WalkID, going to change it anyway with UpdateWalkID, match the field with the
        //DocumentID
        newWalkTwo.WalkID = "a"

        //Add the Walk to the ArrayList in the first position
        hideKeyboard()
        myWalks.add(0, newWalkTwo)
        adapter.notifyItemInserted(0)

        //Add the Walk to FireStore
        CollectionReference.document(loggedInUser)
                //Neil's Walks is a SubCollection in FireStore, containing all of the walks

                .collection(userWalks!!).add(newWalkTwo)
                .addOnSuccessListener { b ->
                    if (b != null) {
                        //Getting the newly created DocID back, storing it in a temp variable
                        someHolder = b.id
                        //Rename the WalkID Field to the DocumentID
                        UpdateWalkId(someHolder) //Call function to change WalkID field of this Walk
                        myWalks[0].WalkID=someHolder //Change WalkID in the local ArrayList
                        Log.d("GettingId", someHolder)
                        tvWalkName.text.clear()
                        tvWalkLocation.text.clear()
                        tvWalkDistance.text.clear()

                    }
                }.addOnFailureListener { e ->
                    Log.e("No ID", "Error Writing Document", e)
                }


    }

    //This Function renames WalkID in a Walk to match the FireStore Document ID, which makes it much easier to
    //modify or delete walks later.
    private fun UpdateWalkId(id: String) { //id parameter is the documentID for the Walk's WalkID we are changing
        CollectionReference.document(loggedInUser)
                .collection(userWalks!!)
                .document((id)).update("walkID", id)
                .addOnSuccessListener { success ->
                    Log.d("Update Worked", "Document updated $success")
                }.addOnFailureListener { e ->
                    Log.e("No ID", "Error Writing Document", e)
                }
    }

    private fun deleteWalk(deletedWalkID: String) {
        CollectionReference.document(loggedInUser)
                .collection(userWalks!!)
                .document(deletedWalkID).delete()
        DisplayWalks() //Get a fresh copy of the Walk ArrayList from FireStore
    }

    //Function gets all the walks for one user from FireStore and returns it in an ArrayList
    private fun DisplayWalks(): ArrayList<Walk> {
        val myWalks = ArrayList<Walk>()
        //val a = FirebaseAuth.getInstance().currentUser!!.uid
        CollectionReference.document(loggedInUser)
                .collection(userWalks!!).get()
                .addOnSuccessListener { b -> //A QuerySnapShot, holds the returned Query Result
                    if (b != null) {
                        for (c in b.documents) { //turn each walk into an Object of type Walk
                            var oneUserWalk = c.toObject<Walk>() //
                            Log.d("Found It", "Document Data: $oneUserWalk and $b")
                            if (oneUserWalk != null) {
                                myWalks.add(oneUserWalk) //Add one walk to the ArrayList
                            }
                        }

                    } else {
                        Log.d("Didn't get it", "Failed to get document")
                    }
                }

        return myWalks
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun update(modelList:ArrayList<Walk>){
        myWalks = modelList
        adapter!!.notifyDataSetChanged()
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

//recyclerView.apply {
//            //DisplayWalks()
//            layoutManager = LinearLayoutManager(this@MainActivity)
//            adapter =  WalksAdapter(myWalks) {partItem:Walk ->
//                partItemClicked(
//                        partItem
//                )
//            }
//        }