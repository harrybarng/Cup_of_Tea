package edu.uw.barngh.cupoftea

import android.content.Intent
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import edu.uw.barngh.cupoftea.R

class GenderInterestActivity : AppCompatActivity() {

    var maleSelect = false
    var femaleSelect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender_interest)

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
            val intent = Intent(this, InterestAcitivity::class.java)
            this.startActivity(intent)
        }

        findViewById<ImageButton>(R.id.male_button).setOnClickListener{
                v ->
            if(femaleSelect){
                femaleSelect = false
                findViewById<ImageButton>(R.id.female_button).setImageResource(R.drawable.bt_female)
            }
            if(!maleSelect){
                maleSelect = true
                findViewById<ImageButton>(R.id.male_button).setImageResource(R.drawable.bt_male_selected)
            }

        }

        findViewById<ImageButton>(R.id.female_button).setOnClickListener{ v ->
            if(maleSelect){
                maleSelect = false
                findViewById<ImageButton>(R.id.male_button).setImageResource(R.drawable.bt_male)
            }
            if(!femaleSelect){
                femaleSelect = true
                findViewById<ImageButton>(R.id.female_button).setImageResource(R.drawable.bt_female_selected)
            }
        }
    }
}
