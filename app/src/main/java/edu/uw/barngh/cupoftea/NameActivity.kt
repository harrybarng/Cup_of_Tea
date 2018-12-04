package edu.uw.barngh.cupoftea

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import edu.uw.barngh.cupoftea.R
import org.w3c.dom.Text

class NameActivity : AppCompatActivity() {

    var name = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        var text = findViewById<EditText>(R.id.user_name)

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
            findViewById<TextView>(R.id.char_counter).text = "${it.length}/20"
            if(it.length > 0 && it.length <= 20){
                findViewById<Button>(R.id.bt_get_started).background = getDrawable(R.drawable.bt_rounded)
                name = true
            }else{
                name = false
                findViewById<Button>(R.id.bt_get_started).background = getDrawable(R.drawable.bt_rounded_unavailable)
            }
        }

        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->

            if(name) {
                var store = findViewById<EditText>(R.id.user_name).text.toString()
                val settings = PreferenceManager.getDefaultSharedPreferences(this)
                settings.edit().putString(getString(R.string.key_user_name), store).apply()
                val intent = Intent(this, MessagePreferenceActivity::class.java)
                this.startActivity(intent)
            }
        }
    }
}
