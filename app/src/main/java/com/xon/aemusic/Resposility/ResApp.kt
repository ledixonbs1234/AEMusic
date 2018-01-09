package com.xon.aemusic.Resposility

import android.util.Log
import com.xon.aemusic.Model.*
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.observable.ObservableDebounceTimed
import io.reactivex.internal.schedulers.NewThreadScheduler
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*
import java.util.function.Consumer
import java.util.regex.Pattern
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList

/**
 * Created by Administrator on 12/11/2017.
 */
class ResApp
private constructor() {
    private object Holder {
        val INSTANCE = ResApp()
    }

    companion object {
        val instance: ResApp by lazy { Holder.INSTANCE }

    }
    init {
        initSingleton()
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

    fun getSourceWeb() : Observable<String>{
        var result = Observable.create(object : ObservableOnSubscribe<String>{
            override fun subscribe(e: ObservableEmitter<String>) {
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
                    Log.d("", "Exception " + Ex.message)
                }
                Log.e("Thong bao",Thread.currentThread().name)
                e.onNext(Result)
            }
        })

        return result
    }


    fun initSingleton(){
        //mSourceWebZingMp3 = GetSourceFromWeb().execute("https://mp3.zing.vn").get()


    }

    lateinit var mImagesBanner : ArrayList<DataWebModel>
    var mSourceWebZingMp3 : String = ""
    lateinit var mDatasDetail : ArrayList<DataSongManager>
    var mCheckedRestoreDetailValue : String = ""

    fun getItemsBanner(source : String) : ArrayList<DataWebModel>
    {
        mImagesBanner = arrayListOf()

        val p = Pattern.compile("id=\"feature\"(\\w|\\W)+?section")
        val m = p.matcher(source)

        var sourceFill = ""
        if (m.find())
            sourceFill = m.group(0)

        val n = Pattern.compile("<li(\\w|\\W)+?href=\"((\\w|\\W)+?)\"(\\w|\\W)+?title=\"((\\w|\\W)+?)\"(\\w|\\W)+?src=\"((\\w|\\W)+?)\"").matcher(sourceFill)
        while (n.find()) {
            mImagesBanner.add(DataWebModel(n.group(8), n.group(2).replace("http:", "https:", false), n.group(5)))
        }
        return mImagesBanner
    }

    fun getItemsPlaylist(source : String,getCount : Int?=-1): ArrayList<DataWebModel> {
        //Khi lay duoc ma nguon xong thi phai loc ra bang regex
        val p = Pattern.compile("row fn-list(\\w|\\W)+?mt20")
        val m = p.matcher(source)
        var sourceFillPlaylist = ""
        //chay regex 1 lan
        if (m.find())
            sourceFillPlaylist = m.group(0)

        //Rút gọn rồi lọc ra danh sách gồm 8 item
        val playlists = ArrayList<DataWebModel>()

        val n = Pattern.compile("item\">(\\W|\\w)+?href=\"((\\W|\\w)+?)\"(\\W|\\w)+?src=\"((\\W|\\w)+?)\" alt=\"((\\W|\\w)+?)\"").matcher(sourceFillPlaylist)

        while (n.find() && playlists.size != getCount) {
            playlists.add(DataWebModel(n.group(5), "https://mp3.zing.vn" + n.group(2), n.group(7)))
        }
        return playlists
    }

    fun getItemBXH(getCount : Int?=-1): ArrayList<DataSongSimple> {
        val source = GetSourceFromWeb().execute("https://mp3.zing.vn/xhr/chart-realtime?chart=song&time=-1&count=5").get()
        //Tao class de chuyen qua json

        //Doi tuong JSONObject goc mieu ta toan bo Json
        val jsonRoot: JSONObject = JSONObject(source)

        //Chuyen data json vao dataWeb
        val dataO = jsonRoot.getJSONObject("data")
        val jsonArrayItem = dataO.getJSONArray("song")

        //Tao cai data Web
        var datas = ArrayList<DataSongSimple>()

        if (getCount != -1) {
            for (i in 0..jsonArrayItem.length() - 1) {

                var jsonItem = jsonArrayItem.getJSONObject(i)

                var data = DataSongSimple(
                        jsonItem.optString("name")
                        , jsonItem.optString("artists_names")
                        , "https://mp3.zing.vn" + jsonItem.optString("link"))

                datas.add(data)
            }
        } else {
            for (i in 0..getCount - 1) {

                var jsonItem = jsonArrayItem.getJSONObject(i)

                var data = DataSongSimple(
                        jsonItem.optString("name")
                        , jsonItem.optString("artists_names")
                        , "https://mp3.zing.vn" + jsonItem.optString("link"))

                datas.add(data)
            }
        }
        return datas
    }

    fun getItemsDetail(url: String): ArrayList<DataSongManager> {

        mDatasDetail = arrayListOf()
        val sourceWeb = GetSourceFromWeb().execute(url).get()

        var tokenPattern = Pattern.compile("id=\"zplayerjs-wrapper\"(\\w|\\W)+?key=((\\w|\\W)+?)\"").matcher(sourceWeb)

        var token: String = ""
        if (tokenPattern.find()) {
            token = tokenPattern.group(2)
        }

        //Lay json
        val source = GetSourceFromWeb().execute("https://mp3.zing.vn/xhr/media/get-source?type=album&key=" + token).get()

        //Doi tuong JSONObject goc mieu ta toan bo Json
        val jsonRoot: JSONObject = JSONObject(source)

        //Chuyen data json vao tao thanh List
        val dataO = jsonRoot.getJSONObject("data")
        val jsonArrayItem = dataO.getJSONArray("items")

        //Tạo list
        //mSongs = arrayListOf()

        for (i in 0..jsonArrayItem.length() - 1) {

            var jsonItem = jsonArrayItem.getJSONObject(i)

            //Lấy chất lượng
            var quality = Quality()

            //Vi khong co https nen them vao thoi
            quality.q128 = "https:" + jsonItem.optJSONObject("source").optString("128")

            var data = DataSongFullSimple(
                    jsonItem.optString("name"),
                    jsonItem.optString("artists_names"),
                    "https://mp3.zing.vn" + jsonItem.optString("link"),
                    null,
                    jsonItem.optString("thumbnail"),
                    quality
            )
            mDatasDetail.add(DataSongManager(data.songName, data, null, null))
        }

        return mDatasDetail
    }

    fun getLinkTrust(link: String) : String{
        var linkTrust : String = ""
        var source : String = GetSourceFromWeb().execute(link).get()
        var n = Pattern.compile("social -->(\\W|\\w)+?\"((\\W|\\w)+?)\"").matcher(source)
        if (n.find()) {
             linkTrust = "https://mp3.zing.vn" + n.group(2)
        }
        return linkTrust
    }

    fun getItemsType(url: String): ArrayList<DataWebModel> {
        var datas : ArrayList<DataWebModel> = arrayListOf()
        var source = GetSourceFromWeb().execute("https://mp3.zing.vn/chu-de").get()
        var n = Pattern.compile("part-cate\"(\\W|\\w)+?footer").matcher(source)
        var sourceFill: String = ""
        if (n.find()) {
            sourceFill = n.group(0)
        }
        var m = Pattern.compile("<a href=\"((\\w|\\W)+?)\" title=\"((\\w|\\W)+?)\"(\\w|\\W)+?src=\"((\\w|\\W)+?)\"").matcher(sourceFill)
        while (m.find()) {
            datas.add(DataWebModel(m.group(6), "https://mp3.zing.vn" + m.group(1), m.group(3)))
        }
        return datas
    }

    fun getItemsTypeDetail(url: String): ArrayList<DataWebModel> {

        var datas : ArrayList<DataWebModel> = arrayListOf()
        var source = GetSourceFromWeb().execute(url).get()
        var n = Pattern.compile("album-item(\\W|\\w)+?href=\"((\\w|\\W)+?)\"(\\w|\\W)+?src=\"((\\w|\\W)+?)\"(\\w|\\W)+?alt=\"((\\w|\\W)+?)\"").matcher(source)

        while (n.find()) {
            datas.add(DataWebModel(n.group(5), "https://mp3.zing.vn" + n.group(2), n.group(8)))
        }

        return datas
    }

    fun getItemPlayer(url: String?): DataSongFullSimple {
        var data : DataSongFullSimple
        var sourceWeb = GetSourceFromWeb().execute(url!!).get()

        var tokenPattern = Pattern.compile("id=\"zplayerjs-wrapper\"(\\w|\\W)+?key=((\\w|\\W)+?)\"").matcher(sourceWeb)

        var token: String = ""
        if (tokenPattern.find()) {
            token = tokenPattern.group(2)
        }

        val source = GetSourceFromWeb().execute("https://mp3.zing.vn/xhr/media/get-source?type=audio&key=" + token).get()

        val jsonRoot = JSONObject(source)

        val objectData = jsonRoot.getJSONObject("data")

        val quality = Quality()

        //Vi khong co https nen them vao thoi
        quality.q128 = "https:" + objectData.optJSONObject("source").optString("128")

        data = DataSongFullSimple(
                objectData.optString("name"),
                objectData.optString("artists_names"),
                objectData.optString("link"),
                null,
                objectData.optString("thumbnail").replace("94_94","240_240"),
                quality
        )

        return data
    }

    fun getItemFromDetail() : ArrayList<DataSongManager> {
        return mDatasDetail
    }

    fun saveDatas(datass: ArrayList<DataSongManager>, mURL: String) {
        mDatasDetail = datass
        mCheckedRestoreDetailValue = mURL
    }


}
