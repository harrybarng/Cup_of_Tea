package edu.uw.barngh.cupoftea

import android.content.Context
import android.content.Intent
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.ImageButton
import edu.uw.barngh.cupoftea.R

class GenderInterestActivity : AppCompatActivity() {

    var maleSelect = false
    var femaleSelect = false
    var finalChoice = "other"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender_interest)

        fun buttonPressed(){
            findViewById<Button>(R.id.bt_get_started).background = getDrawable(R.drawable.bt_rounded)
        }


        findViewById<ImageButton>(R.id.male_button).setOnClickListener{
                v ->
            buttonPressed()
            findViewById<Button>(R.id.bt_get_started).text = "Next"
            if(femaleSelect){
                femaleSelect = false
                findViewById<ImageButton>(R.id.female_button).setImageResource(R.drawable.bt_female)
            }
            if(!maleSelect){
                maleSelect = true
                finalChoice = "male"
                findViewById<ImageButton>(R.id.male_button).setImageResource(R.drawable.bt_male_selected)
            }else{
                maleSelect = false
                finalChoice = "other"
                findViewById<ImageButton>(R.id.male_button).setImageResource(R.drawable.bt_male)
                findViewById<Button>(R.id.bt_get_started).text = "Skip"
            }

        }

        findViewById<ImageButton>(R.id.female_button).setOnClickListener{ v ->
            buttonPressed()
            findViewById<Button>(R.id.bt_get_started).text = "Next"
            if(maleSelect){
                maleSelect = false
                findViewById<ImageButton>(R.id.male_button).setImageResource(R.drawable.bt_male)
            }
            if(!femaleSelect){
                femaleSelect = true
                finalChoice = "female"
                findViewById<ImageButton>(R.id.female_button).setImageResource(R.drawable.bt_female_selected)
            }else{
                femaleSelect = false
                finalChoice = "other"
                findViewById<ImageButton>(R.id.female_button).setImageResource(R.drawable.bt_female)
                findViewById<Button>(R.id.bt_get_started).text = "Skip"
            }
        }

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
                val settings = PreferenceManager.getDefaultSharedPreferences(this)
                settings.edit().putString(getString(R.string.key_user_interested_gender), finalChoice).apply()
                val intent = Intent(this, InterestAcitivity::class.java)
                this.startActivity(intent)

        }
    }
}
