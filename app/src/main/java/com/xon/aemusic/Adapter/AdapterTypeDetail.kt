package com.xon.aemusic.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.xon.aemusic.Interface.OnClickInterface
import com.xon.aemusic.Model.DataWebModel
import com.xon.aemusic.R

/**
 * Created by Administrator on 12/19/2017.
 */
class AdapterTypeDetail : RecyclerView.Adapter<AdapterTypeDetail.ViewHoler> {

    lateinit var mDatas: ArrayList<DataWebModel>
    lateinit var mContextAc : Context
    lateinit var mView : View
    lateinit var listener : OnClickInterface

    constructor(context : Context) : super(){
        mDatas =arrayListOf()
        try {
            mContextAc = context
            listener = mContextAc as OnClickInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "just implement to view")
        }
    }

    fun addData(data: DataWebModel) {
        mDatas.add(data)
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: ViewHoler?, position: Int) {
        holder!!.title.text = mDatas.get(position).title
        Picasso.with(mView.context).load(mDatas.get(position).urlImage).into(holder!!.urlImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHoler {
        var inflater = LayoutInflater.from(parent!!.context)
        mView = inflater.inflate(R.layout.item_type_detail,parent,false)
        return ViewHoler(mView)
    }

    inner class ViewHoler : RecyclerView.ViewHolder{

        var title: TextView
        var urlImage: ImageButton
        constructor(itemView: View?) : super(itemView){
            urlImage = itemView!!.findViewById<ImageButton>(R.id.imagetypedetail)
            urlImage.setOnClickListener(
                    object: View.OnClickListener{
                        override fun onClick(p0: View?) {
                            listener.openDetailOrPlayerFragment(mDatas.get(adapterPosition).urlWeb,null)
                        }
                    })
            title = itemView.findViewById<TextView>(R.id.typedetailname)
        }
    }
}