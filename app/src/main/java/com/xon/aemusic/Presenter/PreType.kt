package com.xon.aemusic.Presenter

import com.xon.aemusic.Interface.ImpType
import com.xon.aemusic.Interface.ViewType
import com.xon.aemusic.Resposility.ResApp

/**
 * Created by Administrator on 12/18/2017.
 */
class PreType : ImpType {


    lateinit var mType : ViewType
    lateinit var mResApp : ResApp
    constructor(view: ViewType)
    {
        mType = view
        mResApp = ResApp.instance
        init()
    }

    private fun init() {
        //Lay dua lieu tu res dua ve view
        mType.requestDataToView(mResApp.getItemsType("https://mp3.zing.vn/chu-de"))
    }

}