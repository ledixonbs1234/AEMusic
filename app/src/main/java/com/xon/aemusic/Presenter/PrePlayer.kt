package com.xon.aemusic.Presenter

import com.xon.aemusic.Interface.ImpPlayer
import com.xon.aemusic.Interface.ViewPlayer
import com.xon.aemusic.Model.DataSongFullSimple
import com.xon.aemusic.Resposility.ResApp

/**
 * Created by Administrator on 12/19/2017.
 */
class PrePlayer : ImpPlayer {

    lateinit var mPlayer : ViewPlayer
    lateinit var mResApp : ResApp

    constructor(view: ViewPlayer)
    {
        this.mPlayer = view
        mResApp = ResApp.instance
        init()
    }

    private fun init() {

    }

    override fun getDataFromView(url: String?) : DataSongFullSimple {
        return mResApp.getItemPlayer(url)
    }


}