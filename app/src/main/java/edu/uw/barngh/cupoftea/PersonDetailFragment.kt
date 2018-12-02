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

        val articleItem = arguments!!.getParcelable<PersonListActivity.Person>(ARTICLE_PARCEL_KEY)

        (rootView.findViewById<View>(R.id.person_heading) as TextView).text =
                if (articleItem.headline == "null") "No heading" else articleItem.headline

        (rootView.findViewById<View>(R.id.person_detail) as TextView).text =
                if (articleItem.description == "null") "No description." else articleItem.description

        (rootView.findViewById<View>(R.id.person_source) as TextView).text =
                if (articleItem.sourceName == "null") "No source" else "Source: ${articleItem.sourceName}"

        val source = (rootView.findViewById<View>(R.id.person_source_link) as TextView)
        source.text = if (articleItem.webUrl == "null") "" else articleItem.webUrl


        setUpToolBar?.setupToolbar()

        return rootView
    }

    companion object {

        val TAG = "MainActivity"

        val ARTICLE_PARCEL_KEY = "article_item"

        fun newInstance(news: PersonListActivity.Person):  PersonDetailFragment {

            val args = Bundle()
            args.putParcelable(ARTICLE_PARCEL_KEY, news)
            val fragment = PersonDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
