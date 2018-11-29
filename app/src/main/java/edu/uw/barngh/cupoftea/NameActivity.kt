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

        // PLEASE DELETE ME, just for test
        // test write new newer
        val DB = FirebaseDB()
        val user = HashMap<String, Any>()
        user["userId"] = "monica"
        user["first"] = "Monica"
        user["last"] = "Ma"
        user["email"] = "gmail.com"
        DB.writeNewUser(user)
        Log.v("newDB", "loading db")
        // test read from database
        var userId = user["userId"]
        DB.readUser(userId as String)
        Log.v("newDB", "loading user's data")

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
            val intent = Intent(this, AgeActivity::class.java)
            this.startActivity(intent)
        }
    }
}
