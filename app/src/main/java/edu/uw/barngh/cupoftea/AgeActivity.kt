package edu.uw.barngh.cupoftea

import android.icu.util.Calendar
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
        datepicker.minDate = mildate - (milInYear*80)
        datepicker.maxDate = mildate - (milInYear*18)
    }
}
