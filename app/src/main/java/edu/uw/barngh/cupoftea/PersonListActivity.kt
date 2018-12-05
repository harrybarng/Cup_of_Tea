package edu.uw.barngh.cupoftea

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Parcelable
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SwitchCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.NetworkImageView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.abc_list_menu_item_checkbox.view.*
import news.uwgin.uw.edu.news.PersonWelcomeFragment
import java.util.Date

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [PersonDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class PersonListActivity : AppCompatActivity() {


    val TAG = "MainActivity"
    private var mTwoPane: Boolean = false
    private val DEFAULT_GENDER = "female"
    private val DEFAULT_GENDER_PREF = "male"
    private val DEFAULT_LONGITUDE = -122.307958f
    private val DEFAULT_LATITUDE = 47.653785f
    private val METERS_TO_MILES_CONVERT_RATE = 1609.34
    private val DEFAULT_DISTANCE = 0
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_list)

        val toolbar = findViewById<View>(R.id.person_list_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        var mDrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setCheckedItem(R.id.nav_home)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            when(menuItem.itemId){
                R.id.nav_home -> {

                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    this.startActivity(intent)
                }
            }
            // close drawer when item is tapped
            mDrawerLayout.closeDrawers()
            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            true
        }

        if (findViewById<View>(R.id.person_detail_container) != null) {
            this.mTwoPane = true
        }

        setFAB("refresh")

        if (this.mTwoPane) {

            if (savedInstanceState != null ) {

                val fragment = PersonWelcomeFragment()
                this.supportFragmentManager.beginTransaction()
                    .replace(R.id.person_detail_container, fragment)
//                    .addToBackStack("root")
                    .commit()
            } else {
                val arguments = Bundle()
                val item: User = intent.extras!!.getParcelable("person_info_item")!!
                arguments.putParcelable("person_info_item", item)
                val fragment = PersonDetailFragment()
                fragment.arguments = arguments
                this.supportFragmentManager.beginTransaction()
                    .replace(R.id.person_detail_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        loadData()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            val settings = PreferenceManager.getDefaultSharedPreferences(this)
            val switch = findViewById<SwitchCompat>(R.id.drawer_switch)
            switch.isChecked = settings.getBoolean(getString(R.string.key_location_visible), true)
        }
        return super.onCreateOptionsMenu(menu)
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


    private fun setFAB(fab_type: String) {
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        val fabRefreshListener = View.OnClickListener {
            loadData()
        }

        when (fab_type) {
            "refresh" -> {
                fab.show()
                fab.setOnClickListener(fabRefreshListener)
            }
            else -> fab.hide()
        }
    }

    private fun loadData() {
        // read data from firebase
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val genderInterested = sharedPref.getString(getString(R.string.key_user_interested_gender), DEFAULT_GENDER_PREF )
        val genderSelf = sharedPref.getString(getString(R.string.key_user_gender), DEFAULT_GENDER)
        val selfName = sharedPref.getString(getString(R.string.key_user_name), "")
        readUserByGender(genderSelf, genderInterested, selfName)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, newsList: List<User>) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, newsList, mTwoPane)
        val spanCount = if (this.mTwoPane) 1 else 2
        recyclerView.layoutManager = GridLayoutManager(this, spanCount)
    }

    private inner class SimpleItemRecyclerViewAdapter internal constructor(private val mParentActivity: PersonListActivity,
                                                                           private val mValues: List<User>,
                                                                           private val mTwoPane: Boolean) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {
        private val showDetail = View.OnClickListener { view ->
            val item = view.tag as User
            if (mTwoPane) {
                val arguments = Bundle()
                setFAB("refresh")
                arguments.putParcelable("person_info_item", item)
                val fragment = PersonDetailFragment()
                fragment.arguments = arguments
                mParentActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.person_detail_container, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                val context = view.context
                val intent = Intent(context, PersonDetailActivity::class.java)
                intent.putExtra("person_info_item", item)
//                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@PersonListActivity).toBundle())
//                val detailImage = findViewById<NetworkImageView>(R.id.detail_image)
                val listImage = findViewById<NetworkImageView>(R.id.list_image)

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@PersonListActivity, view.findViewById(R.id.list_image), "article_image")

                startActivity(intent, options.toBundle())
//                startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.person_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mIdView.text = mValues[position].first_name
//            holder.mContentView.text = mValues[position].description

            holder.itemView.tag = mValues[position]
            holder.itemView.setOnClickListener(showDetail)
            holder.mImageView.setDefaultImageResId(R.drawable.profile_picture_placeholder)

            if (mValues[position].profile_picture != null) {
                holder.mImageView.setImageUrl(mValues[position].profile_picture, VolleyService.getInstance(mParentActivity).imageLoader)
            }
            holder.mAgeView.text = mValues[position].age.toString()
            if (mValues[position].gender == "female") {
                holder.mGenderImage.setImageDrawable(getDrawable(R.drawable.femenine))
            }
            if (mValues[position].location_visible) {
                var distance = getDistance(mValues[position])
                mValues[position].distance = distance
                holder.mLocation.text = distance.toString() + "mile"
            } else {
                holder.mLocation.text = ""
            }
        }

        override fun getItemCount(): Int {
            return mValues.size
        }

        internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mIdView: TextView = view.findViewById<View>(R.id.list_name) as TextView
            //            val mContentView: TextView = view.findViewById<View>(R.id.content) as TextView
            val mImageView: NetworkImageView = view.findViewById(R.id.list_image)
            val mAgeView: TextView = view.findViewById<View>(R.id.list_age) as TextView
            val mGenderImage: ImageView = view.findViewById(R.id.list_gender_img)
            val mLocation: TextView = view.findViewById<View>(R.id.list_location) as TextView
        }
    }

    fun getDistance(user : User) : Int {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val loc1 = Location("")
        loc1.setLatitude(sharedPref.getFloat(getString(R.string.key_location_lat), DEFAULT_LATITUDE).toDouble())
        loc1.setLongitude(sharedPref.getFloat(getString(R.string.key_location_long), DEFAULT_LONGITUDE).toDouble())
        val loc2 = Location("")
        loc2.setLatitude(user.location["lat"]!!)
        loc2.setLongitude(user.location["lng"]!!)
        val distanceInMiles = loc1.distanceTo(loc2) / METERS_TO_MILES_CONVERT_RATE

        return distanceInMiles.toInt()
    }

    @Parcelize
    data class User(
        val first_name:String,
        val last_name:String,
        val age: Int,
        val gender:String,
        val gender_pref:String,
        val location_visible: Boolean,
        val location: MutableMap<String, Double> = mutableMapOf("lat" to 0.toDouble(), "lng" to 0.toDouble()),
        val profile_picture: String,
        val summary: String,
        val interests: String,
        var distance: Int,
        val contact_type: String,
        val contact_value: String

        ) : Parcelable

    var currentUsers = mutableListOf<User>()

    fun getAge(dob: Date): Int {
        val now = Date().time / 1000
        val dobSecond = dob.time / 1000
        return ( (now - dobSecond) / (24 * 60 * 60 * 365) ).toInt()
    }

    fun readUserByGender(genderSelf: String, genderPref: String, selfName: String) {
        db.collection("users")
            .whereEqualTo("gender", genderPref)
            .whereEqualTo("gender_pref", genderSelf)
            .get()
            .addOnSuccessListener { documents ->
                currentUsers.clear()
                for (document in documents) {
                    val age = getAge(document.get("dob") as Date)

                    if (document.get("first_name").toString() == selfName) { // self-exclusive
                        continue;
                    }

                    currentUsers.add(
                        User(
                            document.get("first_name").toString(),
                            document.get("last_name").toString(),
                            age,
                            document.get("gender").toString(),
                            document.get("gender_pref").toString(),
                            document.get("location_visible") as Boolean,
                            document.get("location") as MutableMap<String, Double>,
                            document.get("profile_picture").toString(),
                            document.get("summary").toString(),
                            document.get("interests").toString(),
                            DEFAULT_DISTANCE,
                            document.get("contact_type").toString(),
                            document.get("userId").toString()
                            )
                        )
                }

                Log.d("tag1", "$currentUsers")
                val recyclerView = findViewById<View>(R.id.person_list)!!
                setupRecyclerView(recyclerView as RecyclerView, currentUsers)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}
