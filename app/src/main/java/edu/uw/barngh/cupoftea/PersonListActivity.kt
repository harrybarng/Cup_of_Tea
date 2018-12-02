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
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.webkit.URLUtil
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NetworkImageView

import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_person_list.*
import kotlinx.android.synthetic.main.person_list_content.view.*
import kotlinx.android.synthetic.main.person_list.*
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat

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
//            "userId" to "2062021234",
//            "first_name" to "Claire",
//            "last_name" to "Roberson",
//            "gender" to "female",
//            "gender_pref" to "male",
//            "location" to hashMapOf("lat" to 47.655449,"lng" to -122.307256),
//            "profile_picture" to "https://firebasestorage.googleapis.com/v0/b/cup-of-coffee-401b9.appspot.com/o/profile_pics%2F2062021234.jpeg?alt=media&token=0e040a5e-eaeb-4c4e-a99f-67ea0df5ce3f"
//        )
//        FirebaseDB().writeNewUser(user)
//
//        user = hashMapOf<String, Any>(
//            "userId" to "2062221001",
//            "first_name" to "Michael",
//            "last_name" to "Austin",
//            "gender" to "male",
//            "gender_pref" to "female",
//            "location" to hashMapOf("lat" to 47.632794,"lng" to -122.318786),
//            "profile_picture" to "https://firebasestorage.googleapis.com/v0/b/cup-of-coffee-401b9.appspot.com/o/profile_pics%2F2062221001.jpeg?alt=media&token=074c1eeb-fa31-4aa2-b02f-6e4eed4668c7")
//        FirebaseDB().writeNewUser(user)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_list)

        val toolbar = findViewById<View>(R.id.person_list_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        if (findViewById<View>(R.id.person_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w592dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
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
                val item: Person = intent.extras.getParcelable("article_item")
                setFAB("share", "${item.headline} ${item.webUrl}")
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
//        FirebaseDB().readUser()
//        val data = FirebaseDB().readByGender()
//        Log.d("db1", data.toString())
        this.searchKey = searchKey
        Log.v(TAG, "loading Data")
        val API_KEY = "7e29cdaa58544b90b7dd7c63d7c1a74b"
        val urlString = if (searchKey == ""){
            "https://newsapi.org/v2/top-headlines?country=us&language=en&apiKey=$API_KEY"
        } else {
            "https://newsapi.org/v2/everything?q=$searchKey&language=en&apiKey=$API_KEY"
        }

        val request = JsonObjectRequest(Request.Method.GET, urlString, null,
            Response.Listener { response ->

                val newsList = parseNewsAPI(response)
                val recyclerView = findViewById<View>(R.id.person_list)!!
                setupRecyclerView(recyclerView as RecyclerView, newsList)

            }, Response.ErrorListener { error -> Log.e(TAG, error.toString()) })
        VolleyService.getInstance(this).add(request)
    }


    private fun setupRecyclerView(recyclerView: RecyclerView, newsList: List<Person>) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, newsList, mTwoPane)
        val spanCount = if (this.mTwoPane) 1 else 2
        recyclerView.layoutManager = GridLayoutManager(this, spanCount)
    }

    private inner class SimpleItemRecyclerViewAdapter internal constructor(private val mParentActivity: PersonListActivity,
                                                                           private val mValues: List<Person>,
                                                                           private val mTwoPane: Boolean) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {
        private val showDetail = View.OnClickListener { view ->
            val item = view.tag as Person
            if (mTwoPane) {
                val arguments = Bundle()
                setFAB("share", "${item.headline} ${item.webUrl}")
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
            holder.mIdView.text = mValues[position].headline
//            holder.mContentView.text = mValues[position].description

            holder.itemView.tag = mValues[position]
            holder.itemView.setOnClickListener(showDetail)
            holder.mImageView.setDefaultImageResId(R.drawable.profile_picture_placeholder)

            if (mValues[position].imageUrl != null) {
                holder.mImageView.setImageUrl(mValues[position].imageUrl, VolleyService.getInstance(mParentActivity).imageLoader)
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
    data class Person(
        val headline:String,
        val description:String,
        val publishedTime:Long,
        val webUrl:String,
        val imageUrl:String?,
        val sourceId:String,
        val sourceName: String
    ) : Parcelable


    /**
     * Parses the query response from the News API aggregator
     * https://newsapi.org/
     */
    private fun parseNewsAPI(response: JSONObject):List<Person> {

        val NEWS_ARTICLE_TAG = "Person"

        val stories = mutableListOf<Person>()

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        try {
            val jsonArticles = response.getJSONArray("articles") //response.articles

            for (i in 0 until Math.min(jsonArticles.length(), 20)) { //stop at 20
                val articleItemObj = jsonArticles.getJSONObject(i)

                //handle image url
                var imageUrl:String? = articleItemObj.getString("urlToImage")
                if (imageUrl == "null" || !URLUtil.isValidUrl(imageUrl)) {
                    imageUrl = null //make actual null value
                }

                //handle date
                val publishedTime = try {
                    val pubDateString = articleItemObj.getString("publishedAt")
                    if(pubDateString != "null")
                        formatter.parse(pubDateString).time
                    else
                        0L //return 0
                } catch (e: ParseException) {
                    Log.e(NEWS_ARTICLE_TAG, "Error parsing date", e) //Android log the error
                    0L //return 0
                }

                //access source
                val sourceObj = articleItemObj.getJSONObject("source")

                val story = Person(
                    headline = articleItemObj.getString("title"),
                    webUrl = articleItemObj.getString("url"),
                    description = articleItemObj.getString("description"),
                    imageUrl = imageUrl,
                    publishedTime = publishedTime,
                    sourceId = sourceObj.getString("id"),
                    sourceName = sourceObj.getString("name")
                )

                stories.add(story)
            } //end for loop
        } catch (e: JSONException) {
            Log.e(NEWS_ARTICLE_TAG, "Error parsing json", e) //Android log the error
        }

        return stories
    }
}
