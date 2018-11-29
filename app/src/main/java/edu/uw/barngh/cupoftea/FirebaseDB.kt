package edu.uw.barngh.cupoftea

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class FirebaseDB {
//    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var database: DatabaseReference

    fun writeNewData(userId: String) {
        database = FirebaseDatabase.getInstance().reference

//        database.child("users").child(userId).setValue("hello")
        database.child("users").child(userId).child("username").setValue("Monica")
    }

}