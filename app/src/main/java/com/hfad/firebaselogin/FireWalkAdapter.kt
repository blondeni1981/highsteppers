package com.hfad.firebaselogin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.hfad.firebaselogin.activities.WalksAdapter
import com.hfad.firebaselogin.models.Walk
import kotlinx.android.synthetic.main.walk_row.view.*

class FireWalkAdapter(options: FirestoreRecyclerOptions<Walk>) :
    FirestoreRecyclerAdapter<Walk, FireWalkAdapter.FireWalkVH>(options) {

    class FireWalkVH(itemView: View): RecyclerView.ViewHolder(itemView){
        var distance = itemView.rowDistance
        var location =itemView.rowLocation
        var walkName=itemView.rowName
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FireWalkVH {
        return FireWalkVH(LayoutInflater.from(parent.context).inflate(R.layout.walk_row,parent,false))
    }

    override fun onBindViewHolder(holder:FireWalkVH, position: Int, model:Walk) {
        holder.distance.text=model.WalkDistance
        holder.location.text=model.WalkLocation
        holder.walkName.text=model.WalkName
    }

}