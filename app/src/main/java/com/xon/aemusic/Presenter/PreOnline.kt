package com.xon.aemusic.Presenter

import android.util.Log
import com.xon.aemusic.Interface.ImpOnline
import com.xon.aemusic.Interface.ViewOnline
import com.xon.aemusic.Resposility.ResApp
import com.xon.aemusic.View.Online
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by Administrator on 12/11/2017.
 */
class PreOnline : ImpOnline {



    lateinit var mViewOnline : ViewOnline
    lateinit var mResOnline : ResApp
    constructor(online: ViewOnline)
    {
        this.mViewOnline = online
        this.mResOnline = ResApp.instance
        initOnline()
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

    fun initOnline() {
/*
        Observable.create<String>({e ->
            e.onNext(Thread.currentThread().name)
        }).subscribeOn(Schedulers.io())
                .blockingSubscribe(
                        { e ->
                            Log.e("Thong bao ",e)}
                )*/
        Observable.create<String>({e->
            var Result = ""
            try {

                val URL = URL("https://mp3.zing.vn")
                //params[0]!!.indexOf("https", 0, false) != -1
                //val URL = URL("http://chiasenhac.vn/download2.php?v1=1846&v2=1&v3=1YJD2DG-IGbdYXMX&v4=128&v5=sss.mp3")
                if ("https://mp3.zing.vn"!!.indexOf("https", 0, false) != -1) {
                    val connect = URL.openConnection() as HttpsURLConnection
                    connect.readTimeout = 3000
                    connect.connectTimeout = 3000
                    connect.requestMethod = "GET"
                    connect.connect()

                    val responseCode: Int = connect.responseCode
                    Log.d("Tag", "Response Code " + responseCode)
                    if (responseCode == 200) {
                        val stream: InputStream = connect.inputStream
                        Result = ConvertoString(stream)
                    }
                } else {
                    val connect = URL.openConnection() as HttpURLConnection
                    connect.readTimeout = 3000
                    connect.connectTimeout = 3000
                    connect.requestMethod = "GET"
                    connect.connect()

                    val responseCode: Int = connect.responseCode
                    Log.d("Tag", "Response Code " + responseCode)
                    if (responseCode == 200) {
                        val stream: InputStream = connect.inputStream
                        Result = ConvertoString(stream)
                    }
                }


            } catch (Ex: Exception) {
                e.onError(Ex)
            }
            Log.e("Thong bao",Thread.currentThread().name)
            e.onNext(Result)
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<String> {
            override fun onNext(t: String) {
                Log.e("Thong bao","Res" + Thread.currentThread().name)
                mViewOnline.requestDataToViewbyBanner(mResOnline.getItemsBanner(t))
                mViewOnline.requestDataToViewbyPlaylist(mResOnline.getItemsPlaylist(t,4))
                mViewOnline.requestDataToViewbyBXH(mResOnline.getItemBXH(5))
            }

            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                Log.e("Thong bao",e.message)
            }
        })

    }

    override fun sendLinkAlbumTrust(link: String) : String{
         return mResOnline.getLinkTrust(link)
    }





}

