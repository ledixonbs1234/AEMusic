package com.xon.aemusic.Interface

import com.xon.aemusic.Model.DataSongManager

/**
 * Created by Administrator on 12/21/2017.
 */
interface ViewDownload {
    fun requestDataToView(datas : ArrayList<DataSongManager>)
    fun updateQualityToView(data : DataSongManager)
}