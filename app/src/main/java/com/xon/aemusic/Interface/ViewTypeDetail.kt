package com.xon.aemusic.Interface

import com.xon.aemusic.Model.DataWebModel

/**
 * Created by Administrator on 12/19/2017.
 */
interface ViewTypeDetail {
    fun requestDataFromPresenter(datas : ArrayList<DataWebModel>)
}