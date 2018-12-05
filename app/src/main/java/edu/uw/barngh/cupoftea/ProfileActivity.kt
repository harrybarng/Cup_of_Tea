package edu.uw.barngh.cupoftea

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar = findViewById<View>(R.id.person_list_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }
        //changed selected item
//        findViewById<MenuItem>()

        var mDrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            when(menuItem.itemId){
                R.id.nav_home -> {
                    val intent = Intent(this, PersonListActivity::class.java)
                    this.startActivity(intent)
                }
                R.id.nav_profile -> {

                }
            }
            // close drawer when item is tapped
            mDrawerLayout.closeDrawers()

            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here

            true
        }


        var sharedProfile = PreferenceManager.getDefaultSharedPreferences(this)


        var name = findViewById<TextView>(R.id.detail_name)
//        val sharedPref = this.getSharedPreferences(
//            getString(R.string.key_user_name),
//            Context.MODE_PRIVATE
//        )

//        name.setText(sharedPref.getString(getString(R.string.key_user_name), "name"))
        name.text = sharedProfile.getString(getString(R.string.key_user_name), "Name")

        var summary = findViewById<TextView>(R.id.detail_summary)
        summary.text = sharedProfile.getString(getString(R.string.key_summary), "no summary")


        var age = findViewById<TextView>(R.id.detail_age)
        var day = sharedProfile.getInt(getString(R.string.key_user_birthday), 0)
        var month = sharedProfile.getInt(getString(R.string.key_user_birthmonth), 0)
        var year = sharedProfile.getInt(getString(R.string.key_user_birthyear), 0)
        age.text = "$month / $day / $year"


        var interests = findViewById<TextView>(R.id.detail_interests)
        interests.text = sharedProfile.getString(getString(R.string.key_interests), "no interests")

        var myGenderImg = findViewById<ImageView>(R.id.my_gender_img)
        myGenderImg.setImageDrawable(getDrawable(getGenderImage(sharedProfile.getString(getString(R.string.key_user_gender), "no interests"))))

        var interGenderImg = findViewById<ImageView>(R.id.interested_gender_img)
        myGenderImg.setImageDrawable(getDrawable(getGenderImage(sharedProfile.getString(getString(R.string.key_user_interested_gender), "no interests"))))


        var profileImage = findViewById<ImageView>(R.id.profile_image)
        //set profile image here

        var contactMethod = findViewById<TextView>(R.id.contactValue)
        var method = sharedProfile.getString(getString(R.string.contact_type), "NONE")
        var contactValue = sharedProfile.getString(getString(R.string.contact_value), "")
        contactMethod.text ="$method $contactValue"


        findViewById<Button>(R.id.reset_profile).setOnClickListener { v ->
            val intent = Intent(this, NameActivity::class.java)
            this.startActivity(intent)
        }
//        interests.setText()

    }

    fun getGenderImage(gender: String):Int {
        if(gender == "male") {
            return R.drawable.masculine
        } else if (gender == "female") {
            return R.drawable.femenine
        }
        return R.drawable.gn
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
