package com.xon.aemusic.Interface

import com.xon.aemusic.Model.DataSongSimple
import com.xon.aemusic.Model.DataWebModel

/**
 * Created by Administrator on 12/11/2017.
 */
interface ViewOnline {
    fun requestDataToViewbyBanner(datas : ArrayList<DataWebModel> )
    fun requestDataToViewbyPlaylist(datas : ArrayList<DataWebModel>)
    fun requestDataToViewbyBXH(datas: ArrayList<DataSongSimple>)
}