package edu.uw.barngh.cupoftea

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.preference.PreferenceManager


class SummaryActivity : AppCompatActivity() {

    var next = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        var text = findViewById<EditText>(R.id.summary)

        text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    afterTextChanged.invoke(editable.toString())
                }
            })
        }

        text.afterTextChanged { it ->
            findViewById<TextView>(R.id.char_counter).text = "${it.length}/175"
            if(it.length > 175){
                findViewById<Button>(R.id.bt_get_started).background = getDrawable(R.drawable.bt_rounded_unavailable)
                next = false
            }else if(it.length == 0){
                findViewById<Button>(R.id.bt_get_started).text = "Skip"
                next = false
            }else{
                findViewById<Button>(R.id.bt_get_started).background = getDrawable(R.drawable.bt_rounded)
                findViewById<Button>(R.id.bt_get_started).text = "Next"
                next = true
            }}

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
            if(next) {
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                sharedPref.edit().putString(getString(R.string.key_summary), findViewById<EditText>(R.id.summary).text.toString()).apply()
                val intent = Intent(this, UploadActivity::class.java)
                this.startActivity(intent)
            }
        }
    }
}
