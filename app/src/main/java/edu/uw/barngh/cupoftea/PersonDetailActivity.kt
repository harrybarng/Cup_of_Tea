package edu.uw.barngh.cupoftea

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*

import com.android.volley.toolbox.NetworkImageView

/**
 * An activity representing a single Person detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [PersonListActivity].
 */
class PersonDetailActivity : AppCompatActivity(), PersonDetailFragment.HasCollapsibleToolbar  {


    override fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.detail_toolbar) as Toolbar
        setSupportActionBar(toolbar)


        // Show the Up button in the action bar.
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

        if (savedInstanceState == null){
            val personItem: PersonListActivity.User = intent.extras.getParcelable("person_info_item")
            Log.d("tag1", "in detail activity $personItem")
            val detailFragment = PersonDetailFragment.newInstance(personItem)
            supportFragmentManager.beginTransaction()
                .add(R.id.person_detail_container, detailFragment)
                .commit()

            val detailImage = findViewById<NetworkImageView>(R.id.detail_image)
            detailImage.setDefaultImageResId(R.drawable.profile_picture_placeholder)
            if (personItem.profile_picture!= null || personItem.profile_picture != "" ) {
                detailImage.setImageUrl(
                    personItem.profile_picture,
                    VolleyService.getInstance(this).imageLoader
                )
            }
        }
        else {
            val personItem: PersonListActivity.User = intent.extras.getParcelable("person_info_item")
            Log.d("tag1", "in detail activity else savedInstanceState is not null $personItem")
            val context = this
            val intent = Intent(context, PersonListActivity::class.java)

            val listImage = findViewById<NetworkImageView>(R.id.list_image)

            intent.putExtra("person_info_item", personItem)
            startActivity(intent)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
