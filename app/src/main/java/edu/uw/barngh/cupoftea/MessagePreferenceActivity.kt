package edu.uw.barngh.cupoftea

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.*

class MessagePreferenceActivity : AppCompatActivity() {

    private  var contactPref: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_preference)



        var radioGroup = findViewById<RadioGroup>(R.id.choice)
        radioGroup?.setOnCheckedChangeListener {
            _,checkedId -> val radioButton = findViewById<RadioButton>(checkedId)
            contactPref = checkedId
            findViewById<Button>(R.id.bt_next).text = "Next"
            findViewById<Button>(R.id.bt_next).background = getDrawable(R.drawable.bt_rounded)
            Log.v("contact preference", R.id.email_pref.toString())
            Log.v("contact preference", R.id.text_pref.toString())
            Log.v("contact preference", radioButton.id.toString())
        }



        findViewById<Button>(R.id.bt_next).setOnClickListener { v ->

            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            Log.v("contact preference", contactPref.toString())
            if(contactPref == R.id.email_pref) {
                sharedPref.edit()
                    .putString(getString(R.string.contact_type), "EMAIL").apply()
                sharedPref.edit()
                    .putString(getString(R.string.contact_value), findViewById<EditText>(R.id.email_address).text.toString()).apply()
                Log.v("contact preference",findViewById<EditText>(R.id.email_address).text.toString() )
            } else if (contactPref == R.id.text_pref) {
                sharedPref.edit()
                    .putString(getString(R.string.contact_type), "PHONE").apply()
                sharedPref.edit()
                    .putString(getString(R.string.contact_value), findViewById<EditText>(R.id.text_number).text.toString()).apply()
                Log.v("contact preference",findViewById<EditText>(R.id.text_number).text.toString() )
            }

            val intent = Intent(this, ProfilePictureActivity::class.java)
            this.startActivity(intent)

        }



    }
}
