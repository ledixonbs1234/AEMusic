package com.xon.aemusic.View

import android.app.DownloadManager
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import com.xon.aemusic.Interface.OnClickInterface
import com.xon.aemusic.R
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), OnClickInterface {

    lateinit var mMain : Main
    lateinit var mDetail : Detail
    lateinit var mType : Type
    lateinit var mTypeDetail : TypeDetail
    lateinit var mPlayer : Player
    lateinit var mDownload : Download
    lateinit var mDownloadManager : DownloadManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        initView()
    }

    private fun initView() {
        mMain = Main()
        //Show Fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, mMain, mMain.javaClass.name)
                .addToBackStack(null)
                .commit()
        mDownloadManager = this.getSystemService(android.content.Context.DOWNLOAD_SERVICE) as DownloadManager
        //Checker Permission
        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),1)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 ->{
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else
                {

                }
            }
        }
    }

    override fun downloadFileName(url: String,folder : String, nameFile : String) {
        //http://data.chiasenhac.com/downloads/1858/2/1857873-95030a06/flac/Khong%20Con%20Binh%20Yen%20-%20Juun%20Dang%20Dung%20[Lossless_FLAC].flac
        var request : DownloadManager.Request = DownloadManager.Request(Uri.parse(url))

        //get download file name
        var fileExtension : String = MimeTypeMap.getFileExtensionFromUrl(url)
        var name : String = URLUtil.guessFileName(url,null,fileExtension)

        //save download file name
        request.setDestinationInExternalPublicDir("/"+folder,nameFile)
        var reque : Long = mDownloadManager.enqueue(request)
    }

    override fun backPressed() {
        this.onBackPressed()
    }

    override fun openDetailOrPlayerFragment(linkMp3: String, linkWebDownloadCSN: String?) {
        if (linkMp3.indexOf("album") != -1) {
            mDetail =Detail()
            //Chuyen du lieu vao fragment
            val bundle = Bundle()
            bundle.putString("URL", linkMp3)
            mDetail.arguments = bundle

            supportFragmentManager.beginTransaction()
                    .replace(R.id.main_activity, mDetail, mDetail.javaClass.name)
                    .addToBackStack("detail")
                    .commit()
        } else {
            openPlayer(linkMp3, linkWebDownloadCSN)
        }
    }

    private fun openPlayer(linkMp3: String, linkWebDownloadCSN: String?) {
        mPlayer = Player()

        //Chuyen du lieu vao fragment
        val bundle = Bundle()
        bundle.putString("URL", linkMp3)
        mPlayer.arguments = bundle

        supportFragmentManager.beginTransaction().replace(R.id.main_activity, mPlayer, "Player")
                .addToBackStack(null)
                .commit()
    }

    override fun openTypeFragment() {
        mType = Type()
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, mType, mType.javaClass.name)
                .addToBackStack(null)
                .commit()
    }
    override fun openTypeDetailFragment(urlType: String) {
        mTypeDetail = TypeDetail()

        //Chuyen du lieu vao fragment
        val bundle = Bundle()
        bundle.putString("URL", urlType)
        mTypeDetail.arguments = bundle

        supportFragmentManager.beginTransaction().replace(R.id.main_activity, mTypeDetail, "TypeDetail")
                .addToBackStack(null)
                .commit()
    }

    override fun openDownload() {
        mDownload = Download()

        supportFragmentManager.beginTransaction().replace(R.id.main_activity,mDownload,"Download")
                .addToBackStack(null)
                .commit()
    }
}
