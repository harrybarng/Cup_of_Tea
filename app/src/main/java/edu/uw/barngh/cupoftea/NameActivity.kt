package edu.uw.barngh.cupoftea

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import edu.uw.barngh.cupoftea.R

class NameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        val DB = FirebaseDB()
        DB.writeNewUser("123")
        Log.v("newDB", "loading db")
        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
//            val intent = Intent(this, AgeActivity::class.java)
            val intent = Intent(this, ProfilePictureActivity::class.java)
            this.startActivity(intent)
        }
    }
}
