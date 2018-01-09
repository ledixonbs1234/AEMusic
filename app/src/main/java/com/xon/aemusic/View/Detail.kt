package com.xon.aemusic.View


import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.xon.aemusic.Adapter.AdapterDetail
import com.xon.aemusic.Interface.ImpDetail
import com.xon.aemusic.Interface.OnClickInterface
import com.xon.aemusic.Interface.ViewDetail
import com.xon.aemusic.Model.DataSongManager
import com.xon.aemusic.Presenter.PreDetail

import com.xon.aemusic.R


/**
 * A simple [Fragment] subclass.
 */
class Detail : Fragment(),ViewDetail {


    lateinit var mPreDetail : ImpDetail
    lateinit var mView : View
    lateinit var mURL : String
    lateinit var mRecView : RecyclerView
    lateinit var mAdapter : AdapterDetail
    lateinit var mContextAc : Context
    lateinit var mDownload : Button
    lateinit var listener : OnClickInterface
    lateinit var mLayoutManager : RecyclerView.LayoutManager
    var mListState : Bundle? = null
    final  var INFO_RECYCLERVIEW = "infoReclerview"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                       savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater!!.inflate(R.layout.fragment_detail, container, false)
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPreDetail = PreDetail(this)
        initView()
    }

    override fun onPause() {
        super.onPause()
        mPreDetail.saveDatas(mAdapter.getDatass(),mURL)
    }

    private fun initView() {
        mURL = arguments.getString("URL")

        mRecView = mView.findViewById(R.id.derecview)
        mDownload = mView.findViewById(R.id.download)
        mDownload.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                listener.openDownload()
            }
        })


        mAdapter = AdapterDetail(mContextAc)

        mRecView.adapter = mAdapter

        mLayoutManager = LinearLayoutManager(mView.context,LinearLayoutManager.VERTICAL,false)
        mRecView.layoutManager = mLayoutManager



        if (mPreDetail.isRestored(mURL)) {
            mPreDetail.restoreData()
        }else {

            //Yeu cau presenter thuc hien get data
            mPreDetail.runRequestDataToViewbyURL(mURL)
            //Cap nhat du lieu lay tu Chia se nhac
            mPreDetail.getDatasFromView(mAdapter.getDatass())
        }
    }

    override fun requestDatasToViewbyList(datas: ArrayList<DataSongManager>) {
        for (data in datas) {
            mAdapter.addData(data)
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun updateDataFromPresenter(data: DataSongManager) {
        var datas = mAdapter.getDatass()
        datas.forEach { e ->
            if (e.id == data.id) {
                e.tempSongs = data.tempSongs

                //Lay Item giong chuyen qua main
                for (song in data.tempSongs!!) {
                    if (song.songName.toLowerCase() == e.mainSong.songName.toLowerCase() &&
                            song.artist.replace(';', ',', false).toLowerCase() == e.mainSong.artist.toLowerCase()) {

                        //Thuc hien chuyen bai hat temp da click sang bai hat chinh
                        e.mainSong.artist = song.artist
                        e.mainSong.songName = song.songName
                        e.mainSong.linkWebCSN = song.linkWebDownload
                        e.highQualityShow = song.qualityShow
                        e.linkWebDownloadCSN = song.linkWebDownload
                        return
                    }
                }
                return@forEach
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContextAc = context!!
        listener = mContextAc as OnClickInterface
    }
}// Required empty public constructor
