package com.xon.aemusic.View


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.xon.aemusic.Adapter.AdapterBXH
import com.xon.aemusic.Adapter.AdapterBanner
import com.xon.aemusic.Interface.ImpOnline
import com.xon.aemusic.Interface.OnClickInterface
import com.xon.aemusic.Interface.ViewOnline
import com.xon.aemusic.Model.DataSongSimple
import com.xon.aemusic.Model.DataWebModel
import com.xon.aemusic.Presenter.PreOnline

import com.xon.aemusic.R
import com.xon.aemusic.Resposility.GetSourceFromWeb
import me.relex.circleindicator.CircleIndicator
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 */
class Online : Fragment() , ViewOnline ,View.OnClickListener{


    lateinit var mPreOnline : ImpOnline
    lateinit var mView: View
    lateinit var mViewPagerBanner : ViewPager
    lateinit var mViewPagerBXH : RecyclerView
    lateinit var mAdapterBanner: AdapterBanner
    lateinit var mAdapterBXH : AdapterBXH
    lateinit var mContextAc : Context
    lateinit var mCircleIndicator: CircleIndicator
    lateinit var mPlaylists : ArrayList<DataWebModel>
    lateinit var listener : OnClickInterface

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater!!.inflate(R.layout.fragment_online, container, false)
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        mPreOnline = PreOnline(this)

    }

    private fun initView() {
        mView.findViewById<Button>(R.id.bxhaumy).setOnClickListener(this)
        mView.findViewById<Button>(R.id.bxhhanquoc).setOnClickListener(this)
        mView.findViewById<Button>(R.id.top100).setOnClickListener(this)
        mView.findViewById<Button>(R.id.realtime).setOnClickListener(this)
        mView.findViewById<ImageButton>(R.id.chudevatheloai).setOnClickListener(this)
        //mView.findViewById<ImageButton>(R.id.bxhvietnam).setOnClickListener(this)

        initBanner()
        initBXH()
    }

    private fun initBanner() {
        mViewPagerBanner = view!!.findViewById<ViewPager>(R.id.viewpagerBannerId)
        mAdapterBanner = AdapterBanner(this.context!!, mContextAc)

        mViewPagerBanner.adapter = mAdapterBanner

        mCircleIndicator = mView.findViewById<CircleIndicator>(R.id.cirbanner)

    }

    private fun initBXH(){
        mViewPagerBXH = view!!.findViewById<RecyclerView>(R.id.bangxephang)
        mAdapterBXH = AdapterBXH(mContextAc)

        mViewPagerBXH.adapter = mAdapterBXH
        val layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)

        mViewPagerBXH.layoutManager = layoutManager
    }

    override fun requestDataToViewbyBanner(datas: ArrayList<DataWebModel>) {
        for (data in datas) {
            mAdapterBanner.addData(data)
        }
        mAdapterBanner.notifyDataSetChanged()
        mCircleIndicator.setViewPager(mViewPagerBanner)
    }

    override fun requestDataToViewbyPlaylist(datas: ArrayList<DataWebModel>) {

        mPlaylists = datas
        //Chuyển data ra màn hình
        var imageButton = mView.findViewById<ImageButton>(R.id.onsong1)
        imageButton.setOnClickListener(this)
        Picasso.with(mView.context).load(datas[0].urlImage).into(imageButton)
        mView.findViewById<TextView>(R.id.onsongtext1).text = datas[0].title

        var imageButton2 = mView.findViewById<ImageButton>(R.id.onsong2)
        imageButton2.setOnClickListener(this)
        Picasso.with(mView.context).load(datas[1].urlImage).into(imageButton2)
        mView.findViewById<TextView>(R.id.onsongtext2).text = datas[1].title

        var imageButton3 = mView.findViewById<ImageButton>(R.id.onsong3)
        imageButton3.setOnClickListener(this)
        Picasso.with(mView.context).load(datas[2].urlImage).into(imageButton3)
        mView.findViewById<TextView>(R.id.onsongtext3).text = datas[2].title

        var imageButton4 = mView.findViewById<ImageButton>(R.id.onsong4)
        imageButton4.setOnClickListener(this)
        Picasso.with(mView.context).load(datas[3].urlImage).into(imageButton4)
        mView.findViewById<TextView>(R.id.onsongtext4).text = datas[3].title

    }

    override fun requestDataToViewbyBXH(datas: ArrayList<DataSongSimple>) {
        for (data in datas) {
            mAdapterBXH.addData(data)
        }
        mAdapterBXH.notifyDataSetChanged()
    }




    override fun onClick(p0: View?) {
        var linkDetailWeb : String = ""
        when (p0!!.id) {
            R.id.bxhaumy -> {
                linkDetailWeb = mPreOnline.sendLinkAlbumTrust("https://mp3.zing.vn/zing-chart-tuan/album/IWZ9Z0BW.html")
            }
            R.id.bxhhanquoc -> {
                linkDetailWeb = mPreOnline.sendLinkAlbumTrust("https://mp3.zing.vn/zing-chart-tuan/album/IWZ9Z0BO.html")
            }
            R.id.bxhvietnam -> {
                linkDetailWeb = mPreOnline.sendLinkAlbumTrust("https://mp3.zing.vn/zing-chart-tuan/album/IWZ9Z08I.html")
            }
            R.id.top100 -> {
                linkDetailWeb = "https://mp3.zing.vn/chu-de/Top-100-Hay-Nhat/IWZ9ZI68.html"
            }
            R.id.onsong1 -> {
                linkDetailWeb = mPlaylists.get(0).urlWeb
            }
            R.id.onsong2 -> {
                linkDetailWeb = mPlaylists.get(1).urlWeb
            }
            R.id.onsong3 -> {
                linkDetailWeb = mPlaylists.get(2).urlWeb
            }
            R.id.onsong4 -> {
                linkDetailWeb = mPlaylists.get(3).urlWeb
            }

        }

        if (p0.id == R.id.top100) {
            listener.openTypeDetailFragment(linkDetailWeb)
        }else if (p0.id == R.id.chudevatheloai) {
            listener.openTypeFragment()
        } else {
            listener.openDetailOrPlayerFragment(linkDetailWeb, null)
        }



    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContextAc = context!!
        listener = mContextAc as OnClickInterface
    }
}// Required empty public constructor
