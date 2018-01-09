package com.xon.aemusic.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xon.aemusic.R


/**
 * A simple [Fragment] subclass.
 */
class Offline : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_offline, container, false)
    }

}// Required empty public constructor
