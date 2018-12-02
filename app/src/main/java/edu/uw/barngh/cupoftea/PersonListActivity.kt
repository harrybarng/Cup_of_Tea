package edu.uw.barngh.cupoftea

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NetworkImageView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.parcel.Parcelize

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [PersonDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class PersonListActivity : AppCompatActivity() {

    private var mTwoPane: Boolean = false
    val TAG = "MainActivity"
    private var searchKey = ""
    var db = FirebaseFirestore.getInstance()

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the options menu from XML
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu, menu)
//        // Get the SearchView and set the searchable configuration
//        val searchView = menu.findItem(R.id.search).actionView as android.support.v7.widget.SearchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextChange(newText: String): Boolean {
//                return false
//            }
//            override fun onQueryTextSubmit(query: String): Boolean {
//                //Log.v(TAG, searchView.query.toString())
//
//                loadData(searchView.query.toString())
//                return false
//            }
//        })
//        val expandListener = object : MenuItem.OnActionExpandListener {
//            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
//                Log.v(TAG, "collapsed!")
//                loadData()
//                // Do something when action item collapses
//                return true // Return true to collapse action view
//            }
//
//            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
//                // Do something when expanded
//                Log.v(TAG, "expaneded!")
//                return true // Return true to expand action view
//            }
//        }
//        val searchMenuItem = menu.findItem(R.id.search)
//        searchMenuItem.setOnActionExpandListener(expandListener)
//        return true
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("searchKey", searchKey)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // adding fake data
//        var user = hashMapOf<String, Any>(
//            "userId" to "20612347567",
//            "first_name" to "Benjamin",
//            "last_name" to "Black",
//            "gender" to "male",
//            "gender_pref" to "female",
//            "location" to hashMapOf("lat" to 45.355223,"lng" to -121.412334),
//            "profile_picture" to "https://firebasestorage.googleapis.com/v0/b/cup-of-coffee-401b9.appspot.com/o/profile_pics%2F2062221001.jpeg?alt=media&token=074c1eeb-fa31-4aa2-b02f-6e4eed4668c7"
//        )
//        FirebaseDB().writeNewUser(user)
//
//        user = hashMapOf<String, Any>(
//            "userId" to "20612367892",
//            "first_name" to "Mason",
//            "last_name" to "Zhang",
//            "gender" to "male",
//            "gender_pref" to "female",
//            "location" to hashMapOf("lat" to 46.632794,"lng" to -121.318786),
//            "profile_picture" to "https://firebasestorage.googleapis.com/v0/b/cup-of-coffee-401b9.appspot.com/o/profile_pics%2F2062221002.jpeg?alt=media&token=859f5fc1-0d95-46c0-b045-858802928301")
//        FirebaseDB().writeNewUser(user)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_list)

        val toolbar = findViewById<View>(R.id.person_list_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        if (findViewById<View>(R.id.person_detail_container) != null) {
            this.mTwoPane = true
        }

        if (this.mTwoPane) {

            if (savedInstanceState != null ) {
                setFAB("refresh")
                val fragment = PersonDetailFragment()
                this.supportFragmentManager.beginTransaction()
                    .replace(R.id.person_detail_container, fragment)
//                    .addToBackStack("root")
                    .commit()
            } else {
                val arguments = Bundle()
                val item: User = intent.extras.getParcelable("article_item")
                setFAB("share", "${item.first_name} ${item.last_name}")
                arguments.putParcelable("article_item", item)
                val fragment = PersonDetailFragment()
                fragment.arguments = arguments
                this.supportFragmentManager.beginTransaction()
                    .replace(R.id.person_detail_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        } else {

            if (savedInstanceState != null ) {

            } else {
                setFAB("refresh")
            }
        }

        if (savedInstanceState != null){
            val searchKey = savedInstanceState.getString("searchKey")
            loadData(searchKey)

        } else {
            loadData()
        }
    }

    private fun setFAB(fab_type: String, shareText: String = "") {
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton

        val fabShareListener = View.OnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            startActivity(sendIntent)
        }

        val fabRefreshListener = View.OnClickListener {
            loadData()
        }

        when (fab_type) {
            "share" -> {
                fab.show()
                fab.setOnClickListener(fabShareListener)
//                fab.setImageResource(R.drawable.sh)
            }
            "refresh" -> {
                fab.show()
                fab.setOnClickListener(fabRefreshListener)
            }
            else -> fab.hide()
        }
    }

    private fun loadData(searchKey: String = "") {
        // read data from firebase
        // TODO: need to changed to be user preference
        readUserByGender("female")
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
                setFAB("share", "${item.first_name} ${item.last_name}")
                arguments.putParcelable("article_item", item)
                val fragment = PersonDetailFragment()
                fragment.arguments = arguments
                mParentActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.person_detail_container, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                val context = view.context
                val intent = Intent(context, PersonDetailActivity::class.java)
                intent.putExtra("article_item", item)
//                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@PersonListActivity).toBundle())
//                val detailImage = findViewById<NetworkImageView>(R.id.detail_image)
                val listImage = findViewById<NetworkImageView>(R.id.list_image)

//                val p1 = utilPair.create<View?, String?>(detail_image, "article_image")
//                val p2 = utilPair.create<View?, String?>(list_image, "article_image")

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
        }

        override fun getItemCount(): Int {
            return mValues.size
        }

        internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mIdView: TextView = view.findViewById<View>(R.id.list_heading) as TextView
            //            val mContentView: TextView = view.findViewById<View>(R.id.content) as TextView
            val mImageView: NetworkImageView = view.findViewById(R.id.list_image)

        }
    }

    @Parcelize
    data class User(
        val first_name:String,
        val last_name:String,
        val gender:String,
        val gender_pref:String,
        val location: MutableMap<String, Double> = mutableMapOf(),
        val profile_picture: String
    ) : Parcelable

    var currentUsers = mutableListOf<User>()

    fun readUserByGender(gender: String) {
        db.collection("users")
            .whereEqualTo("gender", gender)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, document.id + " => " + document.data)
                    currentUsers.add(User(document.get("first_name").toString(), document.get("last_name").toString(),
                        document.get("gender").toString(), document.get("gender_pref").toString(),
                        document.get("location") as MutableMap<String, Double>, document.get("profile_picture").toString()))
                }
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
                    Log.d(TAG, "User data: " + document.data!!)
                    currentUsers.add(User(document.get("first_name").toString(), document.get("last_name").toString(),
                        document.get("gender").toString(), document.get("gender_pref").toString(),
                        document.get("location") as MutableMap<String, Double>, document.get("profile_picture").toString()))

                } else {
                    Log.d(TAG, "No such user")
                }
            } else {
                Log.d(TAG, "get failed with ", task.exception)
            }
        }
    }
}
