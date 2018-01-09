package com.xon.aemusic.Presenter

import android.util.Log
import com.xon.aemusic.Interface.ImpDetail
import com.xon.aemusic.Interface.ViewDetail
import com.xon.aemusic.Model.DataSongManager
import com.xon.aemusic.Model.DataSongQualitySimple
import com.xon.aemusic.Model.Qualities
import com.xon.aemusic.Resposility.ResApp
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.Callable
import java.util.regex.Pattern

/**
 * Created by Administrator on 12/15/2017.
 */
class PreDetail : ImpDetail {


    val mDetail : ViewDetail
    lateinit var mResApp : ResApp
    constructor(detail: ViewDetail)
    {
        mDetail = detail
        mResApp = ResApp.instance
        initDetail()
    }

    private fun initDetail() {

    }

    override fun runRequestDataToViewbyURL(url: String) {
        mDetail.requestDatasToViewbyList(mResApp.getItemsDetail(url))
    }

    private fun ConvertoString(stream: InputStream) : String {
        var result = ""
        val isReader = InputStreamReader(stream)
        val bReader = BufferedReader(isReader)

        try {

            result = bReader.readText()
            bReader.close()
            isReader.close()

        } catch (ex: Exception) {
            Log.e("Tag", "Error checking data" + ex.printStackTrace())
        }
        return result
    }


    override fun getDatasFromView(datas: ArrayList<DataSongManager>) {
        var count = 0;
        Observable.create<DataSongManager>({ e ->
            for (data in datas) {
                e.onNext(data)
            }
        })
                .observeOn(Schedulers.io())
                .flatMap({e->
                    val urlCSN = "http://search.chiasenhac.vn/search.php?s=" + URLEncoder.encode(e.id, "UTF-8")
                    //val urlCSN = "http://genk.vn/"
                    return@flatMap Observable.create<String>(
                            {e->
                                var url = URL(urlCSN)
                                val connection = url.openConnection() as HttpURLConnection
                                connection.readTimeout = 10000
                                connection.connectTimeout = 10000
                                connection.requestMethod = "GET"
                                connection.instanceFollowRedirects
                                Log.e("Tag",Thread.currentThread().name)
                                connection.connect()

                                var responseCode = connection.responseCode
                                if (responseCode == 200) {
                                    var stream  = connection.inputStream
                                    e.onNext(ConvertoString(stream) )
                                }else {
                                    "Bi loi"
                                }
                            }
                    )
                })
                .flatMap({e ->
                    var m = Pattern.compile("<a class=\"item\" href=\"((\\w|\\W)+?)\"(\\w|\\W)+?item-title\"><span>((\\w|\\W)+?)<\\/span>(\\w|\\W)+?artist\">((\\w|\\W)+?)<(\\w|\\W)+?quality\">(\\w|\\W)+?>((\\w|\\W)+?)<").matcher(e)
                    var countSearchFinded = 0
                    var tempSongs: ArrayList<DataSongQualitySimple> = arrayListOf()

                    while (m.find() && countSearchFinded < 4) {
                        if (m.group(1).indexOf("video") != -1) {
                            continue
                        }

                        var song = DataSongQualitySimple(m.group(4),
                                m.group(7),
                                coverToQualities(m.group(11)),
                                m.group(1).replace(".html", "_download.html"))

                        tempSongs.add(song)

                        countSearchFinded += 1
                    }

                    return@flatMap Observable.just(tempSongs)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({e ->
                    var data = datas.get(count)
                    Thread.currentThread().name
                    data.tempSongs = e
                    mDetail.updateDataFromPresenter(data)
                    count+=1
                })
    }

    override fun saveDatas(datass: ArrayList<DataSongManager>, mURL: String) {
        mResApp.saveDatas(datass,mURL)
    }

    override fun isRestored(mURL: String?): Boolean {
        if (mURL == mResApp.mCheckedRestoreDetailValue) {
            return true
        }
        return false
    }

    override fun restoreData() {
        mDetail.requestDatasToViewbyList(mResApp.mDatasDetail)
    }

    private fun coverToQualities(quality: String): Qualities {
        when (quality) {
            "Lossless" -> return Qualities.Lossless
            "320kbps" -> return Qualities.q320
            "128kbps" -> return Qualities.q128
            "500kbps" -> return Qualities.q500
        }
        return Qualities.q128
    }
}