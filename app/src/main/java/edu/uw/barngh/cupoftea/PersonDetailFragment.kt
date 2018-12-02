package edu.uw.barngh.cupoftea

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val user = arguments!!.getParcelable<PersonListActivity.User>(USER_PARCEL_KEY)

        (rootView.findViewById<View>(R.id.person_heading) as TextView).text =
                if (user.first_name == "null") "first_name" else user.first_name

        (rootView.findViewById<View>(R.id.person_detail) as TextView).text =
                if (user.last_name == "null") "last_name." else user.last_name

        (rootView.findViewById<View>(R.id.person_source) as TextView).text =
                if (user.gender == "null") "No source" else "${user.gender}"

        val source = (rootView.findViewById<View>(R.id.person_source_link) as TextView)
        source.text = if (user.gender_pref == "null") "" else user.gender_pref

        setUpToolBar?.setupToolbar()

        return rootView
    }

    companion object {

        val USER_PARCEL_KEY = "user"

        fun newInstance(news: PersonListActivity.User):  PersonDetailFragment {

            val args = Bundle()
            args.putParcelable(USER_PARCEL_KEY, news)
            val fragment = PersonDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
