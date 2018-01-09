package com.xon.aemusic.Presenter

import android.content.Context
import android.util.Log
import com.xon.aemusic.Interface.ImpDownload
import com.xon.aemusic.Interface.ViewDownload
import com.xon.aemusic.Model.DataSongManager
import com.xon.aemusic.Model.Quality
import com.xon.aemusic.Resposility.ResApp
import com.xon.aemusic.View.Download
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * Created by Administrator on 12/21/2017.
 */
class PreDownload : ImpDownload {


    lateinit var mDownload : ViewDownload
    lateinit var mResApp : ResApp

    constructor(download : Download){
        this.mDownload = download
        mResApp = ResApp.instance
        init()
    }

    private fun init() {
        mDownload.requestDataToView(mResApp.getItemFromDetail())

    }

    private fun ConvertoString(stream: InputStream): String {
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

    //Lay data tu resapp xu ly tung cai 1 va dua cho view
    override fun requestDatasFromPresenter()   {
        var count :Int = 0
        Observable.create<DataSongManager>({ e->
            for (data in mResApp.mDatasDetail) {
                e.onNext(data)
            }
        })
                .observeOn(Schedulers.io())
                .filter { e ->e.linkWebDownloadCSN != null}
                .flatMap({ e ->
                    val urlCSN = e.linkWebDownloadCSN
                    //val urlCSN = "http://genk.vn/"
                    return@flatMap Observable.create<DataSongManager>(
                            {i->
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
                                    var source = ConvertoString(stream)

                                    var nFill = Pattern.compile("downloadlink2(\\W|\\w)+?center").matcher(source)
                                    if (nFill.find()) {
                                        var n = Pattern.compile("href=\"((\\W|\\w)+?)\"").matcher(nFill.group(0))
                                        //Link dang
                                        //http://chiasenhac.vn/download2.php?v1=1843&v2=1&v3=1YJ2AUA-2XMG2ebG&v4=128&v5=Da%20Lo%20Yeu%20Em%20Nhieu%20-%20JustaTee [128kbps_MP3].mp3

                                        while (n.find()) {

                                            when (n.group(1).substring(n.group(1).indexOf('['), n.group(1).indexOf('[') + 4)) {
                                                "[320" -> {
                                                    e.mainSong.source.q320 = n.group(1)
                                                }
                                                "[500" -> {
                                                    e.mainSong.source.q500 = n.group(1)
                                                }
                                                "[Los" -> {
                                                    e.mainSong.source.lossless = n.group(1)
                                                }
                                            }
                                        }

                                    }
                                    i.onNext(e)

                                }else {
                                    "Bi loi"
                                }
                            }
                    )
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({t ->
                    mDownload.updateQualityToView(t)
                })
    }

}