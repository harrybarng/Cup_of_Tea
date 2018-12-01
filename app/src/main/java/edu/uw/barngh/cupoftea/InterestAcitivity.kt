package edu.uw.barngh.cupoftea

import android.content.Intent
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import edu.uw.barngh.cupoftea.R

class InterestAcitivity : AppCompatActivity() {

    var maleSelect = false
    var femaleSelect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
            val intent = Intent(this, GenderInterestActivity::class.java)
            this.startActivity(intent)
        }
    }
}
