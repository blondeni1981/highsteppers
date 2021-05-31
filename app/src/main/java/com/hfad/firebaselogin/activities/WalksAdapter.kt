package com.hfad.firebaselogin.activities

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.firebaselogin.R
import com.hfad.firebaselogin.models.Walk

import kotlinx.android.synthetic.main.walk_row.view.*

//In the constructor, the Adapter requires a reference to the data source and a click handler,
//wherever it is instructed by the RecyclerView to bind a new ViewHolder, it assigns the clickListener
//as well as the correct values from the data source to the view
//https://www.andreasjakl.com/recyclerview-kotlin-style-click-listener-android/
//clickListener parameter is an instance of a Walk


class WalksAdapter(private val walks: ArrayList<Walk>, private val clickListener: (Walk) -> Unit):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //A ViewHolder represents a single row in the list
    inner class WalkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(a: Walk, clickListener: (Walk) -> Unit) {
                itemView.rowLocation.text=a.WalkLocation
                itemView.rowDistance.text=a.WalkDistance
                itemView.rowName.text=a.WalkName
                itemView.rootView.setOnClickListener{clickListener(a)}
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.walk_row, parent, false)
        return WalkViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WalkViewHolder).bind(walks[position], clickListener)
    }

    override fun getItemCount(): Int = walks.size

    fun removeItem(viewHolder:RecyclerView.ViewHolder):String{
        val tempInt=viewHolder.adapterPosition
        val tempString=walks[tempInt].WalkID
        walks.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
        return tempString
    }


}
//class WalksAdapter(private val walks: ArrayList<Walk>):
//        RecyclerView.Adapter<WalksAdapter.ViewHolder>() {
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val location: TextView = itemView.rowLocation
//        val distance: TextView = itemView.rowDistance
//        val name: TextView = itemView.rowName
//
//
//
//
//
////        override fun onClick(itemView: View) {
////            Log.d("RecyclerView", "Click")
////        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.walk_row, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.location.text = walks[position].WalkName
//        holder.distance.text = walks[position].WalkDistance
//        holder.name.text = walks[position].WalkLocation
//    }
//
//    override fun getItemCount(): Int = walks.size
//
//
//}
