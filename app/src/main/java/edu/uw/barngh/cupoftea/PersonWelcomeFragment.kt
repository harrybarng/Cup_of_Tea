package news.uwgin.uw.edu.news

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.uw.barngh.cupoftea.R


class PersonWelcomeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_person_welcome, container, false)
        return rootView
    }



}
