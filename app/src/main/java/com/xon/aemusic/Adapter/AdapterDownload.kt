package com.xon.aemusic.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.xon.aemusic.Model.DataSongManager
import com.xon.aemusic.R

/**
 * Created by Administrator on 12/21/2017.
 */
class AdapterDownload : RecyclerView.Adapter<AdapterDownload.ViewHoler>  {
    lateinit var mDatas : ArrayList<DataSongManager>
    lateinit var mCheckers : ArrayList<Boolean>

    constructor() : super(){
        mDatas = arrayListOf()

        this.mCheckers = arrayListOf()
    }

    fun addData(data: DataSongManager) {
        this.mDatas.add(data)
        mCheckers.add(false)
    }

    fun getDatasisChecked() : ArrayList<DataSongManager>{
        var dataIsTrue : ArrayList<DataSongManager> = arrayListOf()

        for (i in 0..mCheckers.count() -1 step 1) {
            if (mCheckers.get(i)) {
                dataIsTrue.add(mDatas.get(i))
            }
        }
        return dataIsTrue
    }

    override fun onBindViewHolder(holder: ViewHoler?, position: Int) {
        holder!!.songName.text = mDatas.get(position).mainSong.songName
        holder!!.artist.text = mDatas.get(position).mainSong.artist
        holder!!.count.text = (1 + position).toString()
        holder!!.checker.isChecked = mCheckers.get(position)
        //Thuc hien show quality
        if (!mDatas.get(position).mainSong.source.q320.isNullOrEmpty()) {
            holder!!.download320.visibility = ImageView.VISIBLE
        }
        else
            holder!!.download320.visibility = ImageView.GONE
        if (!mDatas.get(position).mainSong.source.q500.isNullOrEmpty()) {
            holder!!.download500.visibility = ImageView.VISIBLE
        }
        else
            holder!!.download500.visibility = ImageView.GONE
        if (!mDatas.get(position).mainSong.source.lossless.isNullOrEmpty()) {
            holder!!.downloadLossless.visibility = ImageView.VISIBLE
        }
        else
            holder!!.downloadLossless.visibility = ImageView.GONE

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHoler {
        var inflater = LayoutInflater.from(parent!!.context)
        var view = inflater.inflate(R.layout.item_download,parent,false)

        return ViewHoler(view)
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    inner class ViewHoler : RecyclerView.ViewHolder{

        lateinit var songName : TextView
        lateinit var artist : TextView
        lateinit var count : TextView
        lateinit var checker : CheckBox
        var download128 : ImageView
        var download320 : ImageView
        var download500 : ImageView
        var downloadLossless : ImageView

        constructor(itemView: View?) : super(itemView){
            songName = itemView!!.findViewById(R.id.songnamedownload)
            artist = itemView!!.findViewById(R.id.artistdownload)
            count = itemView!!.findViewById(R.id.countdownloadid)
            download128 = itemView!!.findViewById(R.id.download128)
            download320 = itemView!!.findViewById(R.id.download320)
            download500 = itemView!!.findViewById(R.id.download500)
            downloadLossless = itemView!!.findViewById(R.id.downloadLossless)

            checker = itemView!!.findViewById(R.id.checker)
            checker.setOnCheckedChangeListener({c,isChecked ->
                mCheckers.set(adapterPosition,isChecked)
            })
        }
    }
}