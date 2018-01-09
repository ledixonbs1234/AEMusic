package com.xon.aemusic.Resposility

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by Administrator on 12/12/2017.
 */
class GetSourceFromWeb : AsyncTask<String, String, String>() {
    override fun doInBackground(vararg params: String?): String? {
        var Result = ""
        try {

            val URL = URL(params[0])
            //params[0]!!.indexOf("https", 0, false) != -1
            //val URL = URL("http://chiasenhac.vn/download2.php?v1=1846&v2=1&v3=1YJD2DG-IGbdYXMX&v4=128&v5=sss.mp3")
            if (params[0]!!.indexOf("https", 0, false) != -1) {
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
        return Result
    }

    private fun ConvertoString(stream: InputStream): String {
        var result = ""
        val isReader = InputStreamReader(stream)
        val bReader = BufferedReader(isReader)

        try {

            //while (true) {
            //    temp = bReader.readLine()
            //    if (temp == null)
            //        break
            //    result += temp

            result = bReader.readText()
            bReader.close()
            isReader.close()

        } catch (ex: Exception) {
            Log.e("Tag", "Error checking data" + ex.printStackTrace())
        }
        return result
    }
}