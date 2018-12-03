package edu.uw.barngh.cupoftea

import android.app.Person
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Button

class UploadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        findViewById<Button>(R.id.skip_uploading).setOnClickListener {
            val builder= AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)

            builder.setTitle("Cancel uploading?")
                .setMessage("You sure to cancel Uploading? You cannot see list of people if you choose YES.")
                .setPositiveButton("YES") { dialog, which ->

                }
                .setNegativeButton("NO") { dialog, which ->
                    // do nothing
                }
                .setIcon(android.R.drawable.ic_dialog_alert)

            val dialog = builder.create()
            dialog.show()
        }
        uploadData()
    }

    fun goToList() {
        this.startActivity(Intent(this, PersonListActivity::class.java))
    }

    private fun uploadData() {
        val user: HashMap<String, Any> = HashMap()

        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        user["first_name"] = settings.getString(getString(R.string.key_user_name), "") as String
        user["last_name"] = ""
        user["profile_picture"] = settings.getString(getString(R.string.key_profile_picture), "") as String
        // dob
        user["gender"] = settings.getString(getString(R.string.key_user_gender), "") as String
        user["gender_pref"] = settings.getString(getString(R.string.key_user_interested_gender), "") as String
        user["interests"] = settings.getString(getString(R.string.key_interests), "") as String
        user["summary"] = settings.getString(getString(R.string.key_summary), "") as String
        Log.d("tag1", user.toString())
        goToList()
//        val db = FirebaseFirestore.getInstance()
//
////        val userId = user["userId"] as String
//
//        val userId = "oh, just testing"
//        db.collection("users").document(userId)
//            .set(user)
//            .addOnSuccessListener { documentReference ->
//                //                Log.d(
////                    TAG, "A new user added with ID: " + userId
////                )
//                Toast.makeText(this, "Upload Done", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
//            }

    }


}
