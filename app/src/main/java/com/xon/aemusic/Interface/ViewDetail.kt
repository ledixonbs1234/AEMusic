package com.xon.aemusic.Interface

import com.xon.aemusic.Model.DataSongManager

/**
 * Created by Administrator on 12/15/2017.
 */
interface ViewDetail {
    fun requestDatasToViewbyList(datas : ArrayList<DataSongManager>)
    fun updateDataFromPresenter(data : DataSongManager)

}