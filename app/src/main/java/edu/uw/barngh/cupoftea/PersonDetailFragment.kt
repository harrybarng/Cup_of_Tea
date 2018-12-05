package edu.uw.barngh.cupoftea

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceManager
import android.widget.LinearLayout


/**
 * A fragment representing a single Person detail screen.
 * This fragment is either contained in a [PersonListActivity]
 * in two-pane mode (on tablets) or a [PersonDetailActivity]
 * on handsets.
 */
class PersonDetailFragment : Fragment() {

    private var setUpToolBar: HasCollapsibleToolbar? = null

    interface HasCollapsibleToolbar {
        fun setupToolbar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (context is HasCollapsibleToolbar){
            setUpToolBar = context as HasCollapsibleToolbar
        }

        val rootView = inflater.inflate(R.layout.person_detail, container, false)
        Log.d("tag1", "${(arguments == null)}")
        val user = arguments!!.getParcelable<PersonListActivity.User>(USER_PARCEL_KEY)

        (rootView.findViewById<View>(R.id.detail_name) as TextView).text =
                if (user.first_name == "null") "first_name" else "${user.first_name} ${user.last_name}"

        (rootView.findViewById<View>(R.id.detail_age) as TextView).text =
                if (user.age == null) "" else "Age: ${user.age}"

        (rootView.findViewById<View>(R.id.detail_interests) as TextView).text =
                if (user.interests == "null") "" else "Interests: ${user.interests}"

        val summary = (rootView.findViewById<View>(R.id.detail_summary) as TextView)
        summary.text = if (user.summary == "") "No Intro" else user.summary

        if (user.gender == "female") {
            (rootView.findViewById<View>(R.id.detail_gender_img) as ImageView).setImageResource(R.drawable.femenine)
        }

        val distanceAssets = rootView.findViewById<LinearLayout>(R.id.detail_distance_items)
        val navigateButton = rootView.findViewById<Button>(R.id.detail_navigation)
        if (user.location_visible) {
            (rootView.findViewById<View>(R.id.detail_distance) as TextView).text = if (user.location.size == 0) "" else "${user.distance} miles away"
            navigateButton.setOnClickListener {
                val gmmIntentUri = Uri.parse("https://maps.google.com/maps?q=loc:${ user.location["lat"]},${user.location["lng"]}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        } else {
            navigateButton.visibility = View.GONE
            distanceAssets.visibility = View.GONE
        }


        val connectButton= rootView.findViewById<Button>(R.id.detail_connect)
        if (user.contact_type == "PHONE") {
            connectButton.text = "Connect via SMS"

        } else {
            connectButton.text = "Connect via Email"
        }

        connectButton.setOnClickListener {
            val settings = PreferenceManager.getDefaultSharedPreferences(activity)
            val myName = settings.getString(getString(R.string.key_user_name), "cup of tea user")


            if (user.contact_type == "PHONE") {
                val sendIntent = Intent(Intent.ACTION_VIEW)
                sendIntent.data = Uri.parse("sms:")
                sendIntent.putExtra("address", user.contact_value)
                sendIntent.putExtra("sms_body", "Hello, I'm $myName, wanna a cup of coffee?")
                startActivity(sendIntent)
            } else {
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.type = "text/plain"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, user.contact_value)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Connect from cup of tea")
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hello, I'm $myName, wanna a cup of coffee?")
                startActivity(emailIntent)
            }
        }

        setUpToolBar?.setupToolbar()

        return rootView
    }

    companion object {

        val USER_PARCEL_KEY = "person_info_item"

        fun newInstance(person: PersonListActivity.User):  PersonDetailFragment {

            val args = Bundle()
            args.putParcelable(USER_PARCEL_KEY, person)
            val fragment = PersonDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
