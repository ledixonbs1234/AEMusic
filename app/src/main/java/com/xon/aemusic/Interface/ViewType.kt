package com.xon.aemusic.Interface

import com.xon.aemusic.Model.DataWebModel

/**
 * Created by Administrator on 12/18/2017.
 */
interface ViewType {
    fun requestDataToView(datas : ArrayList<DataWebModel>)
}