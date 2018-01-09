package com.xon.aemusic.Model

/**
 * Created by Administrator on 12/16/2017.
 */
class DataSongQualitySimple(var songName: String, var artist: String, var qualityShow: Qualities, var linkWebDownload: String) {

    fun getStringQuality() : String{
        when(qualityShow) {
            Qualities.q128 -> return "128 Kbps"
            Qualities.q320 -> return "320 Kbps"
            Qualities.q500 -> return "500 Kbps"
            Qualities.Lossless -> return "Lossless"
        }
    }
}