package edu.uw.barngh.cupoftea

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseDB {

    private var TAG = "database"

    var db = FirebaseFirestore.getInstance()

    fun writeNewUser(userId: String) {

        val user = HashMap<String, Any>()
        user["first"] = "Monica"
        user["last"] = "Ma"
        user["email"] = "gmail.com"

        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "A new user added with ID: " + userId
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