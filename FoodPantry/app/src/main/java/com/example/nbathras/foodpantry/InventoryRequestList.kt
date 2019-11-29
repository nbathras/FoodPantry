package com.example.nbathras.foodpantry

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.w3c.dom.Text

//Customized adapter for DistributorPageRequestActivity
class DistributorRequestList(private val context:Activity, var requests: List<Request>):
    ArrayAdapter<Request>(context,R.layout.activity_distributor_request_list, requests) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflating donor_splash_list_item layout
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.activity_distributor_request_list, null, true)

        val requestFinishDateText = listViewItem.findViewById<TextView>(R.id.requestFinishDate)

        val request = requests[position]
        requestFinishDateText.text = request.finishDate.toString()

        return listViewItem
    }


}
