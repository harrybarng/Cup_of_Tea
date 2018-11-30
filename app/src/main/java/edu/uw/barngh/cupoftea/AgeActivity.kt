package edu.uw.barngh.cupoftea

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import edu.uw.barngh.cupoftea.R
import java.lang.Long.getLong
import java.util.*

class AgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age)
        var datepicker = findViewById<DatePicker>(R.id.birthdate)
        var date = Calendar.getInstance()
        var mildate = date.timeInMillis
        var milInYear = 31536000000
        datepicker.minDate = mildate - (milInYear*43)
        datepicker.maxDate = mildate - (milInYear*18)

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
            val intent = Intent(this, GenderActivity::class.java)
            this.startActivity(intent)
        }
    }
}
