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
import android.support.v7.widget.SwitchCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.NetworkImageView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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
        navigationView.setCheckedItem(R.id.nav_profile)
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

        val summary = sharedProfile.getString(getString(R.string.key_summary), "")
        findViewById<TextView>(R.id.detail_summary).text = if (summary == "") {
            "No summary"
        } else {
            summary
        }


        var age = findViewById<TextView>(R.id.detail_age)
        var day = sharedProfile.getInt(getString(R.string.key_user_birthday), 0)
        var month = sharedProfile.getInt(getString(R.string.key_user_birthmonth), 0)
        var year = sharedProfile.getInt(getString(R.string.key_user_birthyear), 0)
        age.text = "$month / $day / $year"


        var interests = findViewById<TextView>(R.id.detail_interests)
        interests.text = sharedProfile.getString(getString(R.string.key_interests), "no interests")

        var myGenderImg = findViewById<ImageView>(R.id.my_gender_img)
        myGenderImg.setImageDrawable(getDrawable(getGenderImage(sharedProfile.getString(getString(R.string.key_user_gender), "other"))))

        var interGenderImg = findViewById<ImageView>(R.id.interested_gender_img)
        interGenderImg.setImageDrawable(getDrawable(getGenderImage(sharedProfile.getString(getString(R.string.key_user_interested_gender), "other"))))



        val profileImage = findViewById<NetworkImageView>(R.id.profile_image)
        profileImage.setDefaultImageResId(R.drawable.profile_picture_placeholder)
        val profilePicURL = sharedProfile.getString(getString(R.string.key_profile_picture), "")
        if (profilePicURL != "") {
            profileImage.setImageUrl(profilePicURL, VolleyService.getInstance(this).imageLoader)

        }

        //set profile image here

        var contactMethod = findViewById<TextView>(R.id.contactValue)
        var method = sharedProfile.getString(getString(R.string.contact_type), "NONE")
        var contactValue = sharedProfile.getString(getString(R.string.contact_value), "")
        if(method == "NONE") {
            contactValue = ""
        }
        contactMethod.text ="${method.toLowerCase()}, $contactValue"


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
                val switch = findViewById<SwitchCompat>(R.id.drawer_switch)
                switch.setOnCheckedChangeListener { buttonView, isChecked ->
                    Log.v("hhhh", "3")
                    val locVis = PreferenceManager.getDefaultSharedPreferences(this)

                    if(isChecked){
                        locVis.edit().putBoolean(getString(R.string.key_location_visible), true).apply()
//                        Log.d("tag1", locVis.getString(getString(R.string.contact_value), ""))

                        Toast.makeText(this, "Others can view your location", Toast.LENGTH_SHORT).show()
                    }else{
                        locVis.edit().putBoolean(getString(R.string.key_location_visible), false).apply()
                        Toast.makeText(this, "Others can no longer view your location", Toast.LENGTH_SHORT).show()
                    }

                    val db = FirebaseFirestore.getInstance()
                    val data = HashMap<String, Any>()
                    data["location_visible"] = locVis.getBoolean(getString(R.string.key_location_visible), true)
                    val primaryKey = locVis.getString(getString(R.string.contact_value), "")
                    if (primaryKey != "") {
                        db.collection("users").document(primaryKey)
                            .set(data, SetOptions.merge())
                    }
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
