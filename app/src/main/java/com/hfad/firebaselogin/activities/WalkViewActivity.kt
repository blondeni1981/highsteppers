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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hfad.firebaselogin.R
import com.hfad.firebaselogin.firebase.FirestoreClass
import com.hfad.firebaselogin.models.Walk
import com.hfad.firebaselogin.utils.Constants
import com.hfad.firebaselogin.utils.MarginItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_walk_view.*

class WalkViewActivity : AppCompatActivity() {

    private val db = Firebase.firestore //Instantiate an instance of a FireStore
    private var CollectionReference = db.collection(Constants.USERS) //Points to the users collection
    private val loggedInUser = FirebaseAuth.getInstance().currentUser!!.uid //get the currently logged in userID
    private var userWalks=FirebaseAuth.getInstance().currentUser!!.email+" Walks"
    var myWalks = DisplayWalks()
    //Create an instance of WalksAdapter, pass in ArrayList & a CallBack function, for itemClick Events
    //The WalksAdapter is the RecyclerView Adapter, its the engine that powers the RecyclerView
    private val adapter = WalksAdapter(myWalks) { partItem: Walk ->
        partItemClicked(
            partItem
        )
    }
    //var bundle: Bundle? = intent.extras





    override fun onCreate(savedInstanceState: Bundle?) {
        //https://medium.com/@huih1108/android-kotlin-use-parcelable-to-pass-object-to-another-activity-bbc552b07972
        val mybundle = intent.getBundleExtra("DISPLAY_WALK")
        var myObject= mybundle?.getParcelable<Walk>("key")
        //Pointing to RV in XML, then attaching the RV adapter from WalksAdapter class to it


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_view)

        myRecyclerView.adapter = adapter
        //Item Decoration add margin between ViewHolders in the RecyclerView
        myRecyclerView.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.row_card_recyclerlayout))
        )
        //Choosing which layout manager we want, grid/linear
        myRecyclerView.layoutManager = LinearLayoutManager(this@WalkViewActivity)
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
        itemTouchHelper.attachToRecyclerView(myRecyclerView)



        }


    //Hides Keyboard immediately, so the user can see the Toast, called above
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun deleteWalk(deletedWalkID: String) {
        CollectionReference.document(loggedInUser)
            .collection(userWalks!!)
            .document(deletedWalkID).delete()
        DisplayWalks() //Get a fresh copy of the Walk ArrayList from FireStore
    }

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
    //https://www.journaldev.com/37763/android-intent-handling-between-activities-using-kotlin
    private fun partItemClicked(oneWalk: Walk) {
        Toast.makeText(this, "Clicked, $oneWalk", Toast.LENGTH_LONG).show()
        //This line below is interesting, but not wanted, makes a new list, but can't easily pass in the Intent
        //var filterID = myWalks.filter { s -> s.WalkID == partItem.WalkID }

        //Create a new Intent and pass it Display Activity, putExtra adds the walk
//        val intent = Intent(this, WalkViewActivity::class.java)
//        val bundle =Bundle()
//        bundle.putParcelable("key", oneWalk)
//        intent.putExtra("DISPLAY_WALK", bundle)
//        startActivity(intent)
        //Parceable must be included in the model (Walk.kt) to allow the serialization of objects
        //to be passed from one activity to another in Intents

    }











}
