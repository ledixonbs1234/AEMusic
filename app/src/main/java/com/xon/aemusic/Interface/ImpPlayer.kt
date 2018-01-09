package com.xon.aemusic.Interface

import com.xon.aemusic.Model.DataSongFullSimple

/**
 * Created by Administrator on 12/19/2017.
 */
interface ImpPlayer {
    fun getDataFromView(url: String?) : DataSongFullSimple
}