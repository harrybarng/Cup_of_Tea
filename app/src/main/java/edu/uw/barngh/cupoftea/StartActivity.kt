package edu.uw.barngh.cupoftea

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
//            val intent = Intent(this, NameActivity::class.java)
            val intent = Intent(this, NameActivity::class.java)
            this.startActivity(intent)
        }
        

    }
}
