package edu.uw.barngh.cupoftea

import android.content.Context
import android.content.Intent
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.BoringLayout
import android.widget.*
import edu.uw.barngh.cupoftea.R


class InterestAcitivity : AppCompatActivity() {

    var pickCounter = 0
    var checker : MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)

        for(i in 1..6) {
            checker.add("")
        }


        fun buttonState(){
            if(pickCounter == 3){
                findViewById<Button>(R.id.bt_get_started).background = getDrawable(R.drawable.bt_rounded)
            }else{
                findViewById<Button>(R.id.bt_get_started).background = getDrawable(R.drawable.bt_rounded_unavailable)
            }
        }

        fun checkState(interest : Int, name : String, isChecked :Boolean){
            if(isChecked){
                pickCounter++
                checker.set(interest, name)
            }else{
                pickCounter--
                checker.set(interest, "")
            }
            buttonState()
        }

        findViewById<CheckBox>(R.id.hiking).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(0, "Hiking",isChecked)
        }

        findViewById<CheckBox>(R.id.movie).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(1,"Movie", isChecked)
        }

        findViewById<CheckBox>(R.id.eating).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(2,"Eating", isChecked)
        }

        findViewById<CheckBox>(R.id.gaming).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(3, "Gaming", isChecked)
        }

        findViewById<CheckBox>(R.id.cuddling).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(4, "Cuddling", isChecked)
        }

        findViewById<CheckBox>(R.id.cooking).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(5, "Cooking", isChecked)
        }

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
            if(pickCounter == 3) {

                var interestString = ""
                var counter = 0
                for (i in 0..5){
                    if(checker[i] != ""){
                        interestString += checker[i]
                        if(counter < 2){
                            interestString += ", "
                        }
                        counter++
                    }

                }
                val settings = PreferenceManager.getDefaultSharedPreferences(this)
                settings.edit().putString(getString(R.string.key_interests), interestString).apply()
                Toast.makeText(this, interestString, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SummaryActivity::class.java)
                this.startActivity(intent)
            }
        }

    }
}
