package edu.uw.barngh.cupoftea

import android.content.Context
import android.content.Intent
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import edu.uw.barngh.cupoftea.R

class GenderActivity : AppCompatActivity() {

    var maleSelect = false
    var femaleSelect = false
    var finalChoice = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender)

        fun buttonPressed(){
            findViewById<Button>(R.id.bt_get_started).background = getDrawable(R.drawable.bt_rounded)
        }

        findViewById<ImageButton>(R.id.male_button).setOnClickListener{
                v ->
            buttonPressed()
            if(femaleSelect){
                femaleSelect = false
                findViewById<ImageButton>(R.id.female_button).setImageResource(R.drawable.bt_female)
            }
            if(!maleSelect){
                maleSelect = true
                finalChoice = "male"
                findViewById<ImageButton>(R.id.male_button).setImageResource(R.drawable.bt_male_selected)
            }

        }

        findViewById<ImageButton>(R.id.female_button).setOnClickListener{ v ->
            buttonPressed()
            if(maleSelect){
                maleSelect = false
                findViewById<ImageButton>(R.id.male_button).setImageResource(R.drawable.bt_male)
            }
            if(!femaleSelect){
                femaleSelect = true
                finalChoice = "female"
                findViewById<ImageButton>(R.id.female_button).setImageResource(R.drawable.bt_female_selected)
            }
        }

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
            if(finalChoice != ""){
                val sharedPref = this.getSharedPreferences(
                    getString(R.string.key_user_gender),
                    Context.MODE_PRIVATE
                )
                sharedPref.edit().putString(getString(R.string.key_user_gender), finalChoice).commit()
                val intent = Intent(this, GenderInterestActivity::class.java)
                this.startActivity(intent)
            }
        }


    }
}
