package com.example.cafe.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.cafe.R
import com.example.cafe.datas.Data
import com.example.cafe.datas.DetailData

class DetailAdapter (val mContext : Context, val resId : Int, val dList: List<DetailData>): ArrayAdapter<DetailData>(mContext,resId,dList) {

    val inf = LayoutInflater.from(mContext)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tmpRow = convertView
        val listPosition = dList[position]


        if(tmpRow == null){
            tmpRow = inf.inflate(R.layout.item_list,null)
        }
        val row = tmpRow!!

        val listName = row.findViewById<TextView>(R.id.listName)


        //listName.text = listPosition.storeName

        return row
    }

}