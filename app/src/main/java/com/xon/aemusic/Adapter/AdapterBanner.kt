package com.xon.aemusic.Adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.Log
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
 * Created by Administrator on 12/11/2017.
 */
class AdapterBanner(var context: Context, contextAc: Context) : PagerAdapter() {

    var inflater: LayoutInflater
    // Lam 1 cai activity tai day
    var listener: OnClickInterface
    var mImages: ArrayList<DataWebModel>

    init {
        inflater = LayoutInflater.from(context)
        mImages = arrayListOf()

        try {
            listener = contextAc as OnClickInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement on View Selected")
        }


    }

    fun addData(data: DataWebModel) {
        mImages.add(data)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        //Inflate no nhan them cai imagebanner vao fragment.xml tung cai 1
        val view: View = inflater.inflate(R.layout.item_image_banner, container, false)

        //Trong fragment.xml da nhan duoc imageButton tu imageBanner.xml
        val imageButton: ImageButton = view.findViewById<ImageButton>(R.id.imageId)
        val txtTitle: TextView = view.findViewById<TextView>(R.id.titleid)
        //imageButton.setImageResource(lstImages.get(position))
        Picasso.with(view.context).load(mImages.get(position).urlImage).into(imageButton)
        txtTitle.text = mImages.get(position).title

        imageButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.e("Chao", mImages.get(position).toString())

                //Lam cac cong viec dua dia chi vao day
                //Tao intent
                //var intent  = Intent(view.context,DetailActivity::class.java)
                //intent.putExtra("URLWeb",lstImages.get(position).urlWeb)
                //view.context.startActivity(intent)

                listener.openDetailOrPlayerFragment(mImages.get(position).urlWeb,null)
            }
        })

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun getCount(): Int {
        return mImages.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}



