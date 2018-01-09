package com.xon.aemusic.View


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xon.aemusic.Adapter.AdapterType
import com.xon.aemusic.Interface.ViewType
import com.xon.aemusic.Model.DataWebModel
import com.xon.aemusic.Presenter.PreType

import com.xon.aemusic.R


/**
 * A simple [Fragment] subclass.
 */
class Type : Fragment() , ViewType {

    lateinit var mPreType : PreType
    lateinit var mRecView : RecyclerView
    lateinit var mView : View
    lateinit var mAdapterType : AdapterType
    lateinit var mContextAc : Context

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater!!.inflate(R.layout.fragment_type, container, false)
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        mPreType = PreType(this)
    }

    private fun initView() {
        mRecView = mView.findViewById(R.id.rectype)

        mAdapterType = AdapterType(mContextAc)

        mRecView.adapter = mAdapterType

        var layout = GridLayoutManager(mView.context,2)
        mRecView.layoutManager = layout

    }

    override fun requestDataToView(datas: ArrayList<DataWebModel>) {
        for (data in datas) {
            mAdapterType.addData(data)
        }
        mAdapterType.notifyDataSetChanged()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContextAc = context!!
    }
}// Required empty public constructor

