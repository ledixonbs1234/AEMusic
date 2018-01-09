package com.xon.aemusic.View


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xon.aemusic.Adapter.AdapterViewPager

import com.xon.aemusic.R


/**
 * A simple [Fragment] subclass.
 */
class Main : Fragment() {

    lateinit var mviewPager: ViewPager
    lateinit var mTabLayout: TabLayout
    lateinit var mOnline: Online
    lateinit var mOffline: Offline
    lateinit var viewMain: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewMain = inflater!!.inflate(R.layout.fragment_main, container, false)

        initView()
        return viewMain
    }

    private fun initView() {
        mviewPager = viewMain.findViewById(R.id.viewpagerid)

        val adapter = AdapterViewPager(childFragmentManager)

        mOffline = Offline()
        mOnline = Online()

        adapter.addFragment(Online(), "Online")
        adapter.addFragment(Offline(), "Offline")
        mviewPager.adapter = adapter


        mTabLayout = viewMain.findViewById(R.id.tabs)

        mTabLayout.setupWithViewPager(mviewPager)

    }


}// Required empty public constructor
