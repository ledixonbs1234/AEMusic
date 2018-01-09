package com.xon.aemusic.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.xon.aemusic.Interface.OnClickInterface
import com.xon.aemusic.Model.DataSongFullSimple
import com.xon.aemusic.Model.DataSongManager
import com.xon.aemusic.Model.Qualities
import com.xon.aemusic.Model.Quality
import com.xon.aemusic.R
import com.xon.aemusic.Resposility.SingletonPlayMusic

class AdapterDetail : RecyclerView.Adapter<AdapterDetail.RecyclerViewHolder> {

    val mDatas: ArrayList<DataSongManager>
    var listener: OnClickInterface
    lateinit var mSingleton : SingletonPlayMusic

    constructor(context: Context) : super() {
        this.mDatas = arrayListOf()
        mSingleton = SingletonPlayMusic.instance
        try {
            listener = context as OnClickInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement on View Selected")
        }
    }

    fun addData(data: DataSongManager) {
        mDatas.add(data)

    }

    fun getDatass():ArrayList<DataSongManager>{
        return mDatas
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewHolder {
        var inflater = LayoutInflater.from(parent!!.context)
        var view = inflater.inflate(R.layout.item_detail, parent, false)
        //view.setOnClickListener(mOnclickListener)
        return RecyclerViewHolder(inflater.context, view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder?, position: Int) {
/*
        if (mDatas.size != 0) {
            //Lấy link từ ChiaseNhac
            if (mDatas[position].tempSongs == null) {
                mDatas[position].tempSongs = TaskerDetail().execute(mDatas[position].id).get()

                //Thuc hien kiem tra va ra cai moi nhat
                for (song in mDatas[position].tempSongs!!) {
                    if (song.songName.toLowerCase() == mDatas[position].mainSong.songName.toLowerCase() &&
                            song.artist.replace(';', ',', false).toLowerCase() == mDatas[position].mainSong.artist.toLowerCase()) {

                        //Thuc hien chuyen bai hat temp da click sang bai hat chinh
                        mDatas[position].mainSong.artist = song.artist
                        mDatas[position].mainSong.songName = song.songName
                        mDatas[position].mainSong.linkWebCSN = song.linkWebDownload
                        mDatas[position].highQualityShow = song.qualityShow
                        mDatas[position].linkWebDownloadCSN = song.linkWebDownload
                    }
                }
            }
            if (mDatas[position].mainSong.songName.length > 20) {
                holder!!.songnameid!!.text = mDatas[position].mainSong.songName.substring(0, 20) + "..."
            } else
                holder!!.songnameid!!.text = mDatas[position].mainSong.songName

            holder.tencasiid!!.text = mDatas[position].mainSong.artist
            holder.countid!!.text = (position + 1).toString()
            holder.quality.text = covertQualitiesToText(mDatas[position].highQualityShow)
        }
        */

        if (mDatas[position].mainSong.songName.length > 20) {
            holder!!.songnameid!!.text = mDatas[position].mainSong.songName.substring(0, 20) + "..."
        } else
            holder!!.songnameid!!.text = mDatas[position].mainSong.songName

        holder!!.tencasiid!!.text = mDatas.get(position).mainSong.artist
        holder.countid!!.text = (position + 1).toString()
        holder.quality.text = Quality().covertQualitiesToText(mDatas.get(position).highQualityShow)
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    inner class RecyclerViewHolder : RecyclerView.ViewHolder {

        var songnameid: TextView? = null
        var tencasiid: TextView? = null
        var countid: TextView? = null
        var download: ImageButton
        var contexttemp: Context
        var quality: TextView

        constructor(context: Context, itemView: View) :
                super(itemView) {
            songnameid = itemView.findViewById(R.id.songnameid)
            tencasiid = itemView.findViewById(R.id.tencasiid)
            countid = itemView.findViewById(R.id.countid)
            download = itemView.findViewById(R.id.btndownloadmusic)
            quality = itemView.findViewById(R.id.qualitydetail)

            contexttemp = context


            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    if (!v.isActivated) {
                        var detail = mDatas.get(adapterPosition)
                        Toast.makeText(contexttemp, songnameid!!.text, Toast.LENGTH_LONG).show()
                        var data: ArrayList<DataSongFullSimple> = arrayListOf()
                        mDatas.forEach { n -> data.add(n.mainSong) }
                        //Lay list neu co
                        mSingleton.mMusics = data
                        mSingleton.mCurrentMusicNoSource = detail.mainSong
                        //callback
                        listener.openDetailOrPlayerFragment(detail.mainSong.linkMp3, null)

                        //callback
                        //listener.openBarMusic(detail)
                    }
                }
            })

            download.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    var dialog : AlertDialog.Builder = AlertDialog.Builder(itemView.context)
                    dialog.setTitle("Chọn bài hát")

                    //var inflater = LayoutInflater.from(itemView.context)
                    //var view = inflater.inflate(R.layout.item_dialog,null)
                    //dialog.setView(view)

                    var adapter = AdapterDialog(itemView.context)

                    dialog.setAdapter(adapter,object : DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            mDatas.get(adapterPosition).mainSong.songName = adapter.mDatas.get(p1).songName
                            mDatas.get(adapterPosition).mainSong.artist = adapter.mDatas.get(p1).artist
                            mDatas.get(adapterPosition).mainSong.linkWebCSN = adapter.mDatas.get(p1).linkWebDownload
                            mDatas.get(adapterPosition).highQualityShow = adapter.mDatas.get(p1).qualityShow
                            mDatas.get(adapterPosition).linkWebDownloadCSN = adapter.mDatas.get(p1).linkWebDownload
                            notifyDataSetChanged()
                        }
                    })

                    for (data in mDatas.get(adapterPosition).tempSongs!!) {
                        adapter.addData(data)
                    }

                    dialog.show()

                }
            })

/*
            download.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {

                    //Tao sepuence item
                    //val Animals = datass.toArray(arrayOfNulls<String>(datass.size))

                    var dialog: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
                    dialog.setTitle("Chọn bài hát")

                    //var inflater : LayoutInflater = LayoutInflater.from(itemView.context)
                    //var dialogView : View = inflater.inflate(R.layout.dialog_item,null)
                    //dialog.setView(dialogView)
                    var songManager = mDatas.get(adapterPosition)
                    var detail: ArrayList<DataSongQualitySimple>? = songManager.tempSongs
                    var mainSong = songManager.mainSong

                    dialog.setAdapter(AdapterDialog(detail!!, itemView.context), object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            Toast.makeText(itemView.context, "Vi tri thu" + p1, Toast.LENGTH_LONG).show()

                            //Thuc hien chuyen bai hat temp da click sang bai hat chinh
                            mainSong.artist = detail.get(p1).artist
                            mainSong.songName = detail.get(p1).songName
                            mainSong.linkWebCSN = detail.get(p1).linkWebDownload
                            songManager.linkWebDownloadCSN = detail.get(p1).linkWebDownload
                            songManager.highQualityShow = detail.get(p1).qualityShow


                        }
                    })

                    //dialogView.findViewById<TextView>(R.id.songnamedialog).text = "Test"
                    //dialogView.findViewById<TextView>(R.id.tencasidialog).text = "Test dcccc"

                    //dialog.setItems(Animals,object : DialogInterface.OnClickListener{
                    //    override fun onClick(p0: DialogInterface?, p1: Int) {
                    //        var text : String = Animals[p1].toString()
                    //    }
                    //})

                    //Tao dialog
                    var alertDialogObject: AlertDialog = dialog.create()
                    //Hien len
                    alertDialogObject.show()
                }
            })*/

        }

    }

}