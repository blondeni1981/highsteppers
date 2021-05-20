package com.hfad.firebaselogin.models

import android.os.Parcel
import android.os.Parcelable

//https://www.sitepoint.com/transfer-data-between-activities-with-android-parcelable/
//Parcelable is a Plugin for serializing objects to be passed from one activity to another in Intents
data class User (
    val id: String ="",
    val name: String = "",
    val email: String = "",
    val image: String =""
        ): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(dest: Parcel, flags: Int) =with(dest) {
        writeString(id)
        writeString(name)
        writeString(email)
        writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}
