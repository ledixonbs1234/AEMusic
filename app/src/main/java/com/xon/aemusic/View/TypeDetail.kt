package com.xon.aemusic.View


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xon.aemusic.Adapter.AdapterTypeDetail
import com.xon.aemusic.Interface.ImpTypeDetail
import com.xon.aemusic.Interface.ViewTypeDetail
import com.xon.aemusic.Model.DataWebModel
import com.xon.aemusic.Presenter.PreTypeDetail

import com.xon.aemusic.R


/**
 * A simple [Fragment] subclass.
 */
class TypeDetail : Fragment() ,ViewTypeDetail{


    lateinit var mView : View
    lateinit var mPreTypeDetail : ImpTypeDetail
    lateinit var mRecView : RecyclerView
    lateinit var mAdapter : AdapterTypeDetail
    lateinit var mContextAc : Context
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView =inflater!!.inflate(R.layout.fragment_type_detail, container, false)
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPreTypeDetail = PreTypeDetail(this)
        init()

    }

    private fun init() {
        mRecView = mView.findViewById(R.id.rectypedetail)

        mAdapter = AdapterTypeDetail(mContextAc)

        mRecView.adapter = mAdapter

        var layoutManager = GridLayoutManager(this.context, 3)
        mRecView.layoutManager = layoutManager

        var url = arguments.getString("URL")
        mPreTypeDetail.getUrlFromView(url)
    }

    override fun requestDataFromPresenter(datas: ArrayList<DataWebModel>) {
        for (data in datas) {
            mAdapter.addData(data)
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContextAc = context!!
    }
}// Required empty public constructor
