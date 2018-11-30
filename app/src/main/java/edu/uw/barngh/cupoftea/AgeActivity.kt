package edu.uw.barngh.cupoftea

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import edu.uw.barngh.cupoftea.R
import org.w3c.dom.Text
import java.lang.Long.getLong
import java.util.*

class AgeActivity : AppCompatActivity() {

    private var TAG = "AgeActivity"
    private var year : Int = Calendar.YEAR
    private var month : Int = Calendar.MONTH
    private var dayofMonth : Int = Calendar.DAY_OF_MONTH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age)
//        var datepicker = findViewById<DatePicker>(R.id.birthdate)
//        var date = Calendar.getInstance()
//        var mildate = date.timeInMillis
//        var milInYear = 31536000000
//        datepicker.minDate = mildate - (milInYear*80)
//        datepicker.maxDate = mildate - (milInYear*18)

        var displayDate = findViewById<TextView>(R.id.showDate)
        var chooseButton = findViewById<Button>(R.id.choose)
        chooseButton.setOnClickListener { view ->
            var cal : Calendar = Calendar.getInstance()
            var dialog : DatePickerDialog = DatePickerDialog(
                this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
                    displayDate.setText("" + (mMonth + 1) + "/" + mDay + "/" + mYear)
                    year = mYear
                    month = mMonth
                    dayofMonth = mDay
                }, year, month, dayofMonth
                )
            dialog.datePicker.minDate = cal.timeInMillis - (31536000000*100)
            dialog.datePicker.maxDate = cal.timeInMillis - (31536000000*18)
            dialog.show()
        }
    }
}
