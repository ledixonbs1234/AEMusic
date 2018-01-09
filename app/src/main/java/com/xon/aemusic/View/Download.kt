package com.xon.aemusic.View


import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.xon.aemusic.Adapter.AdapterDownload
import com.xon.aemusic.Interface.ImpDownload
import com.xon.aemusic.Interface.OnClickInterface
import com.xon.aemusic.Interface.ViewDownload
import com.xon.aemusic.Model.DataSongManager
import com.xon.aemusic.Model.Qualities
import com.xon.aemusic.Presenter.PreDownload

import com.xon.aemusic.R


/**
 * A simple [Fragment] subclass.
 */
 class Download:Fragment(), ViewDownload , View.OnClickListener {

    lateinit var mView : View
    lateinit var mPreDownload : ImpDownload
    lateinit var mRecView : RecyclerView
    lateinit var mAdapter : AdapterDownload
    lateinit var listener : OnClickInterface
    lateinit var mFolder : EditText
    lateinit var mContextAc : Context
    lateinit var mCurrentQuality : Qualities
    lateinit var mQuality : TextView
    lateinit var mMenu : PopupMenu


        public override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?,
            savedInstanceState:Bundle?):View? {
            // Inflate the layout for this fragment
            mView = inflater!!.inflate(R.layout.fragment_download, container, false)
            return mView
        }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        mPreDownload = PreDownload(this)
    }

    private fun init() {
        mCurrentQuality = Qualities.q128
        mRecView = mView.findViewById(R.id.downloadRec)
        mView.findViewById<Button>(R.id.downloadtaive).setOnClickListener(this)
        mFolder = mView.findViewById(R.id.folder)
        mView.findViewById<Button>(R.id.downloadthoat).setOnClickListener(this)
        mView.findViewById<Button>(R.id.downloadcheckall).setOnClickListener(this)
        mQuality = mView.findViewById(R.id.downloadquality)
        mQuality.setOnClickListener(this)
        mMenu = PopupMenu(mView.context, mQuality )

        mMenu.menu.add(Menu.NONE, 1, Menu.NONE, "128 Kbs")
        mMenu.menu.add(Menu.NONE, 1, Menu.NONE, "320 Kbs")
        mMenu.menu.add(Menu.NONE, 1, Menu.NONE, "500 Kbs")
        mMenu.menu.add(Menu.NONE, 1, Menu.NONE, "Lossless")
        mMenu.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(p0: MenuItem?): Boolean {
                if (p0!!.title == "Lossless") {
                    setCurrentQuality(Qualities.Lossless)
                    mQuality.text = "Lossless"
                } else if (p0!!.title == "500 Kbs") {
                    mQuality.text = "500 Kbs"
                    setCurrentQuality(Qualities.q500)
                } else if (p0!!.title == "320 Kbs") {
                    mQuality.text = "320 Kbs"
                    setCurrentQuality(Qualities.q320)
                } else if (p0!!.title == "128 Kbs") {
                    mQuality.text = "128 Kbs"
                    setCurrentQuality(Qualities.q128)
                }
                return true
            }
        })



        mAdapter = AdapterDownload()
        mRecView.adapter = mAdapter
        var layout = LinearLayoutManager(mView.context,LinearLayoutManager.VERTICAL,false)
        mRecView.layoutManager = layout
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.downloadtaive ->
                for (data in mAdapter.getDatasisChecked()) {
                    val urlChanged = GetUrlCurrentQuality(data)
                    val extension = urlChanged.substring(urlChanged.lastIndexOf('.'),urlChanged.length)
                    //Thuc hien tai ve]\
                    //Kiểm tra chất lượng hiện tại và tải về bằng chất lượng đó
                    when (mCurrentQuality) {
                        Qualities.q128 ->
                            listener.downloadFileName(data.mainSong.source.q128,mFolder.text.toString(),data.id+ " .mp3")
                        Qualities.q320 ->
                            listener.downloadFileName(urlChanged,mFolder.text.toString(),data.id+ extension )
                        Qualities.q500 ->
                            listener.downloadFileName(urlChanged,mFolder.text.toString(),data.id+extension)
                        Qualities.Lossless ->
                            listener.downloadFileName(urlChanged,mFolder.text.toString(),data.id+ extension)
                    }
                }
            R.id.downloadthoat ->
                listener.backPressed()
            R.id.downloadcheckall -> {
                for (i in 0..mAdapter.mCheckers.size - 1 step 1) {
                    mAdapter.mCheckers.set(i, true)
                }
                mAdapter.notifyDataSetChanged()
            }
            R.id.downloadquality ->
                    mMenu.show()
        }
    }

    fun setCurrentQuality(quality: Qualities) {
        mCurrentQuality = quality
        if (quality == Qualities.q320 || quality == Qualities.q500 || quality == Qualities.Lossless) {
            //Thuc hien show bang tuy chon
            var builder = AlertDialog.Builder(mView.context)
            builder.setTitle("Thông báo")
            builder.setMessage("Bạn có muốn tải chất lượng này không.\n Nếu có thì vui lòng đợi xíu.")
            builder.setPositiveButton("Có",object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    //Thuc hien lấy dữ liệu trong này
                    Toast.makeText(mView.context,"Đang lấy dữ liệu vui lòng chờ xíu.",Toast.LENGTH_LONG)
                    mPreDownload.requestDatasFromPresenter()
                }
            }).setNegativeButton("Không", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            })
            builder.show()

        }
    }

    fun GetUrlCurrentQuality(data : DataSongManager) : String{

        var urlDefault = data.mainSong.source.getSourceQualityCurrent(mCurrentQuality).source
        if (urlDefault.substring(0, 5) == "https") {
            return urlDefault
        } else {
            var condition = urlDefault.substring(urlDefault.length-4,urlDefault.length)
            if (condition == "flac")
                return urlDefault.substring(0,urlDefault.indexOf("flac/")+5) +"nhacLossless.flac"
            else if (condition == ".m4a")
                return urlDefault.substring(0,urlDefault.indexOf("m4a/")+4) +"nhac500.m4a"
            else
                return urlDefault.substring(0,urlDefault.indexOf("320/")+4) +"nhac128or320.mp3"
        }

    }

    override fun requestDataToView(datas : ArrayList<DataSongManager>) {
        for (data in datas) {
            mAdapter.addData(data)
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun updateQualityToView(data: DataSongManager) {
        mAdapter.mDatas.forEach { e->
            if (e.id == data.id) {
                e.mainSong.source = data.mainSong.source
                return@forEach
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as OnClickInterface
        mContextAc = context
    }
}// Required empty public constructor
