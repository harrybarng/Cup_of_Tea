package edu.uw.barngh.cupoftea

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_person_detail.*
import kotlinx.android.synthetic.main.person_detail.view.*

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

        val distance = (rootView.findViewById<View>(R.id.detail_distance) as TextView)
        distance.text = if (user.location.size == 0) "" else "${user.distance} miles away"

        val summary = (rootView.findViewById<View>(R.id.detail_summary) as TextView)
        summary.text = if (user.summary == "null") "" else user.summary

        if (user.gender == "female") {
            (rootView.findViewById<View>(R.id.detail_gender_img) as ImageView).setImageResource(R.drawable.femenine)
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
