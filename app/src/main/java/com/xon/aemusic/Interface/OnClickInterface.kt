package com.xon.aemusic.Interface

/**
 * Created by Administrator on 12/11/2017.
 */
interface OnClickInterface {
    fun openDetailOrPlayerFragment(linkMp3: String, linkWebDownloadCSN: String?)
    fun openTypeFragment()
    fun openTypeDetailFragment(urlType: String)
    fun openDownload()
    fun downloadFileName(url : String,folder : String, nameFile : String)
    fun backPressed()
}