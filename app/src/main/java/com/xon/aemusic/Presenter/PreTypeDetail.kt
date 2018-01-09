package com.xon.aemusic.Presenter

import com.xon.aemusic.Interface.ImpTypeDetail
import com.xon.aemusic.Interface.ViewTypeDetail
import com.xon.aemusic.Resposility.ResApp

/**
 * Created by Administrator on 12/19/2017.
 */
class PreTypeDetail : ImpTypeDetail {

    lateinit var mTypeDetail : ViewTypeDetail
    lateinit var mResApp : ResApp

    constructor(view : ViewTypeDetail){
        mTypeDetail = view
        mResApp = ResApp.instance
    }

    override fun getUrlFromView(url: String) {
        mTypeDetail.requestDataFromPresenter(mResApp.getItemsTypeDetail(url))
    }

}