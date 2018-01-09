package com.xon.aemusic.Interface

import com.xon.aemusic.Model.DataSongManager

/**
 * Created by Administrator on 12/15/2017.
 */
interface ImpDetail {
    fun runRequestDataToViewbyURL(url: String)
    fun getDatasFromView(datas : ArrayList<DataSongManager>)
    fun saveDatas(datass: ArrayList<DataSongManager>, mURL: String)
    fun isRestored(mURL: String?): Boolean
    fun restoreData()
}