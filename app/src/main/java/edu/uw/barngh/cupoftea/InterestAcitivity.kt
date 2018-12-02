package edu.uw.barngh.cupoftea

import android.content.Intent
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import edu.uw.barngh.cupoftea.R


class InterestAcitivity : AppCompatActivity() {

    var pickCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)



        fun buttonState(){
            if(pickCounter == 3){
                findViewById<Button>(R.id.bt_get_started).background = getDrawable(R.drawable.bt_rounded)
            }else{
                findViewById<Button>(R.id.bt_get_started).background = getDrawable(R.drawable.bt_rounded_unavailable)
            }
        }

        fun checkState(isChecked :Boolean){
            if(isChecked){
                pickCounter++
            }else{
                pickCounter--
            }
            buttonState()
        }

        findViewById<CheckBox>(R.id.hiking).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(isChecked)
        }

        findViewById<CheckBox>(R.id.movie).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(isChecked)
        }

        findViewById<CheckBox>(R.id.eating).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(isChecked)
        }

        findViewById<CheckBox>(R.id.gaming).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(isChecked)
        }

        findViewById<CheckBox>(R.id.cuddling).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(isChecked)
        }

        findViewById<CheckBox>(R.id.cooking).setOnCheckedChangeListener { buttonView, isChecked ->
            checkState(isChecked)
        }

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
            if(pickCounter == 3) {
                val intent = Intent(this, SummaryActivity::class.java)
                this.startActivity(intent)
            }
        }

    }
}
