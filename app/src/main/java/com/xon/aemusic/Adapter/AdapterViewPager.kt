package com.xon.aemusic.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Administrator on 12/11/2017.
 */
class AdapterViewPager : FragmentPagerAdapter {

    var fragments: ArrayList<Fragment> = ArrayList<Fragment>()
    var titles: ArrayList<String> = ArrayList<String>()

    constructor(fm: FragmentManager) : super(fm)

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles.get(position)
    }
}