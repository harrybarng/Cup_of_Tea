package edu.uw.barngh.cupoftea

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

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
        user["gender"] = settings.getString(getString(R.string.key_user_gender), "") as String
        user["gender_pref"] = settings.getString(getString(R.string.key_user_interested_gender), "") as String
        user["interests"] = settings.getString(getString(R.string.key_interests), "") as String
        user["summary"] = settings.getString(getString(R.string.key_summary), "") as String
        user["contact_type"] = settings.getString(getString(R.string.contact_type), "PHONE") as String

        // dob
        val dobYear = settings.getInt(getString(R.string.key_user_birthyear), 1990)
        val dobMonth = settings.getInt(getString(R.string.key_user_birthmonth), 1)
        val dobDay = settings.getInt(getString(R.string.key_user_birthday), 1)

        val dob = Calendar.getInstance()
        dob.set(Calendar.YEAR, dobYear)
        dob.set(Calendar.MONTH, dobMonth)
        dob.set(Calendar.DAY_OF_MONTH, dobDay)
        dob.set(Calendar.HOUR_OF_DAY, 0)
        dob.set(Calendar.MINUTE, 0)
        dob.set(Calendar.SECOND, 0)
        dob.set(Calendar.MILLISECOND, 0)

        user["dob"] = Date(dob.timeInMillis)

        // location
        val lat = settings.getFloat(getString(R.string.key_location_lat), 47.608013F)
        val lng = settings.getFloat(getString(R.string.key_location_long), (-122.335167).toFloat())
        user["location"] = hashMapOf(
                "lat" to lat,
                "lng" to lng
            )

        settings.edit().putBoolean(getString(R.string.key_location_visible), true).apply()
        user["location_visible"] = settings.getBoolean(getString(R.string.key_location_visible), true)
        val userId = settings.getString(getString(R.string.contact_value), "2062224312") as String
        user["userId"] = userId

        val db = FirebaseFirestore.getInstance()

        Log.d("tag1", "$user")
        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener { documentReference ->
                settings.edit().putBoolean(getString(R.string.key_setup_done), true).apply()
                goToList()
                Toast.makeText(this, "Upload Done", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
            }
    }
}
