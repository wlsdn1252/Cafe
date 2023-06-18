package com.example.cafe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.cafe.datas.Data

//class Adapter(val mContext : Context, val resId : Int, val mList: List<Data>): ArrayAdapter<Data>(mContext,resId,mList) {
//
//    val inf = LayoutInflater.from(mContext)
//
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var tmpRow = convertView
//        val listPosition = mList[position]
//
//
//        if(tmpRow == null){
//            tmpRow = inf.inflate(R.layout.item_list,null)
//        }
//        val row = tmpRow!!
//
//        val listName = row.findViewById<TextView>(R.id.listName)
//        val listAdd = row.findViewById<TextView>(R.id.listAdd)
//        val listoldAdd = row.findViewById<TextView>(R.id.listoldAdd)
//        val listwido = row.findViewById<TextView>(R.id.listwido)
//        val listgd = row.findViewById<TextView>(R.id.listgd)
//
//
//        listName.text = listPosition.storeName
//        listAdd.text = listPosition.newAddress
//        listoldAdd.text = listPosition.oldAddress
//        listwido.text = listPosition.latitude.toString()
//        listgd.text = listPosition.hardness.toString()
//
//
//        return row
//    }
//
//}

class Adapter (val mContext : Context, val resId : Int, val mList: List<Data>): ArrayAdapter<Data>(mContext,resId,mList) {

    val inf = LayoutInflater.from(mContext)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tmpRow = convertView
        val listPosition = mList[position]


        if(tmpRow == null){
            tmpRow = inf.inflate(R.layout.item_list,null)
        }
        val row = tmpRow!!

        val listName = row.findViewById<TextView>(R.id.listName)


        listName.text = listPosition.storeName




        return row
    }

}