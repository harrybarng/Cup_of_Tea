package edu.uw.barngh.cupoftea

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.os.Parcelable
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.toolbox.NetworkImageView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.parcel.Parcelize
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
    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {

        // adding fake data
//        var user = hashMapOf<String, Any>(
//            "userId" to "2061234752",
//            "first_name" to "Ross",
//            "last_name" to "Black",
//            "gender" to "male",
//            "gender_pref" to "female",
//            "age" to 25,
//            "location" to hashMapOf("lat" to 43.355223,"lng" to -122.412334),
//            "summary" to "A machine learning hacker!",
//            "profile_picture" to "https://firebasestorage.googleapis.com/v0/b/cup-of-coffee-401b9.appspot.com/o/profile_pics%2F2062221001.jpeg?alt=media&token=074c1eeb-fa31-4aa2-b02f-6e4eed4668c7"
//        )
//        FirebaseDB().writeNewUser(user)
//
//        user = hashMapOf<String, Any>(
//            "userId" to "2061236783",
//            "first_name" to "Ken",
//            "last_name" to "Wang",
//            "gender" to "male",
//            "gender_pref" to "female",
//            "age" to 21,
//            "location" to hashMapOf("lat" to 46.632794,"lng" to -121.318786),
//            "summary" to "I hate raining but love Seattle!",
//            "profile_picture" to "https://firebasestorage.googleapis.com/v0/b/cup-of-coffee-401b9.appspot.com/o/profile_pics%2F2062221002.jpeg?alt=media&token=859f5fc1-0d95-46c0-b045-858802928301")
//        FirebaseDB().writeNewUser(user)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_list)

        val toolbar = findViewById<View>(R.id.person_list_toolbar) as Toolbar
        setSupportActionBar(toolbar)


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
//                Log.d("tag1", "$item")
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
        readUserByGender(genderSelf, genderInterested)
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


        }
    }

    @Parcelize
    data class User(
        val first_name:String,
        val last_name:String,
        val age: Int,
        val gender:String,
        val gender_pref:String,
        val location: MutableMap<String, Double> = mutableMapOf(),
        val profile_picture: String,
        val summary: String,
        val interests: String


        ) : Parcelable

    var currentUsers = mutableListOf<User>()

    fun getAge(dob: Date): Int {
        val now = Date().time / 1000
        val dobSecond = dob.time / 1000
        return ( (now - dobSecond) / (24 * 60 * 60 * 365) ).toInt()
    }

    fun readUserByGender(genderSelf: String, genderPref: String) {
        val selfGender =
        db.collection("users")
            .whereEqualTo("gender", genderSelf)
            .whereEqualTo("gender_pref", genderPref)
            .get()
            .addOnSuccessListener { documents ->
                currentUsers.clear()
                for (document in documents) {
//                    Log.d(TAG, document.id + " => " + document.data)
                    val age = getAge(document.get("dob") as Date)


                    currentUsers.add(User(document.get("first_name").toString(), document.get("last_name").toString(),
                        age, document.get("gender").toString(), document.get("gender_pref").toString(),
                        document.get("location") as MutableMap<String, Double>, document.get("profile_picture").toString(),
                        document.get("summary").toString(), document.get("interests").toString() ))
                }

                Log.d("tag1", "$currentUsers")
                val recyclerView = findViewById<View>(R.id.person_list)!!
                setupRecyclerView(recyclerView as RecyclerView, currentUsers)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun readUserById(userId : String) {
        val docRef = db.collection("users").document(userId)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document!!.exists()) {

                    val age = getAge(document.get("dob") as Date)

                    currentUsers.add(User(document.get("first_name").toString(), document.get("last_name").toString(),
                        age, document.get("gender").toString(), document.get("gender_pref").toString(),
                        document.get("location") as MutableMap<String, Double>, document.get("profile_picture").toString(),
                        document.get("summary").toString(), document.get("interests").toString()))

                } else {
                    Log.d(TAG, "No such user")
                }
            } else {
                Log.d(TAG, "get failed with ", task.exception)
            }
        }
    }
}
