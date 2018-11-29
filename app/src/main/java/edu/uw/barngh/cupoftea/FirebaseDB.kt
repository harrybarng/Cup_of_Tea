package edu.uw.barngh.cupoftea

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore




class FirebaseDB {
    var db = FirebaseFirestore.getInstance()

    fun writeNewUser(userId: String) {

        val user = HashMap<String, Any>()
        user["first"] = "Monica"
        user["last"] = "Ma"
        user["email"] = "gmial.com"

        db.collection("users")
            .add(user)
    }


//    fun writeNewData(userId: String) {
//        database = FirebaseDatabase.getInstance().reference
//
////        database.child("users").child(userId).setValue("hello")
//        database.child("users").child(userId).child("username").setValue("Monica")
//    }

}