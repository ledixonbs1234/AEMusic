package com.xon.aemusic.Adapter

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.TextView
import com.xon.aemusic.Model.DataSongManager
import com.xon.aemusic.Model.DataSongQualitySimple
import com.xon.aemusic.R

/**
 * Created by Administrator on 12/30/2017.
 */
class AdapterDialog : BaseAdapter {

    var mDatas : ArrayList<DataSongQualitySimple>
    var context : Context

    constructor(view : Context){
        mDatas = arrayListOf()
        context = view
    }

    fun addData(data: DataSongQualitySimple) {
        mDatas.add(data)
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflater.inflate(R.layout.item_dialog, null)
        view.findViewById<TextView>(R.id.songnamedialog).text = mDatas.get(p0).songName
        view.findViewById<TextView>(R.id.tencasidialog).text = mDatas.get(p0).artist
        view.findViewById<TextView>(R.id.qualitydialog).text = mDatas.get(p0).getStringQuality()
        view.findViewById<TextView>(R.id.countdialogid).text = (p0 + 1).toString()
        return view
    }


    override fun getCount(): Int {
        return mDatas.size
    }

    override fun getItem(p0: Int): Any {
        return mDatas.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
}