package edu.uw.barngh.cupoftea

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseDB {

    private var TAG = "database1"

    var db = FirebaseFirestore.getInstance()

    var currentData = mutableListOf<MutableMap<String,Any>>()

    fun writeNewUser(user: HashMap<String, Any>) {

        var userId = user["userId"] as String

        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG, "A new user added with ID: " + userId
                )
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding user", e) }
    }

    fun readUser(userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document!!.exists()) {
                    Log.d(TAG, "User data: " + document.data!!)
                } else {
                    Log.d(TAG, "No such user")
                }
            } else {
                Log.d(TAG, "get failed with ", task.exception)
            }
        }
    }
}
