package com.example.nbathras.foodpantry

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

//Customized adapter for DistributorPageRequestActivity
class DistributorRequestList(private val context:Activity, var requests: List<Request>):
    ArrayAdapter<Request>(context,R.layout.activity_distributor_request_list, requests) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflating donor_splash_list_item layout
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.activity_distributor_request_list, null, true)

        val textViewName = listViewItem.findViewById<View>(R.id.requestItemName) as TextView
        val textViewAbout = listViewItem.findViewById<View>(R.id.quantityItemLabel) as TextView

        val request = requests[position]
        textViewName.text = request.requestName
        textViewAbout.text = request.currRequestNum.toString()

        return listViewItem
    }


}
