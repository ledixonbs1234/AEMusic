package com.xon.aemusic.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.xon.aemusic.Interface.OnClickInterface
import com.xon.aemusic.Model.DataSongSimple
import com.xon.aemusic.Model.DataWebModel
import com.xon.aemusic.R

/**
 * Created by Administrator on 12/15/2017.
 */
class AdapterBXH : RecyclerView.Adapter<AdapterBXH.RecyclerViewHolder> {
    private var datas: ArrayList<DataSongSimple>
    var listener: OnClickInterface
    //private final var mOnclickListener : View.OnClickListener = View.OnClickListener()

    constructor(context: Context) : super() {
        this.datas = arrayListOf()
        try {
            listener = context as OnClickInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement on View Selected")
        }
    }

    fun addData(data: DataSongSimple) {
        datas.add(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewHolder {
        var inflater = LayoutInflater.from(parent!!.context)
        var view = inflater.inflate(R.layout.item_bxh, parent, false)
        //view.setOnClickListener(mOnclickListener)
        return RecyclerViewHolder(inflater.context, view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder?, position: Int) {
        holder!!.songnameid!!.text = datas[position].songName
        holder.tencasiid!!.text = datas[position].artist
        holder.countid!!.text = (position + 1).toString()
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    inner class RecyclerViewHolder : RecyclerView.ViewHolder {

        var songnameid: TextView? = null
        var tencasiid: TextView? = null
        var countid: TextView? = null
        var contexttemp: Context

        constructor(context: Context, itemView: View) :
                super(itemView) {
            songnameid = itemView.findViewById(R.id.songnameid)
            tencasiid = itemView.findViewById(R.id.tencasiid)
            countid = itemView.findViewById(R.id.countid)
            contexttemp = context
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    var detail = datas.get(adapterPosition)
                    Toast.makeText(contexttemp, songnameid!!.text, Toast.LENGTH_LONG).show()

                    //callback
                    listener.openDetailOrPlayerFragment(detail.linkSong, null)
                }
            })
        }

    }
}