package com.example.nbathras.foodpantry

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

//Customized adapter for DistributorSplashActivity
class RequestList(val context:Activity, var requests: ArrayList<HashMap<String,Any>>):
    ArrayAdapter<HashMap<String,Any>>(context,R.layout.request_list_item, requests) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflating distributor_splash_list_item layout
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.request_list_item, null, true)

        val requestItemName = listViewItem.findViewById<TextView>(R.id.requestItemName)
        val requestItemCurr = listViewItem.findViewById<TextView>(R.id.requestItemCurrent)
        val requestItemDesired = listViewItem.findViewById<TextView>(R.id.requestItemDesired)

        val request = requests[position]

        requestItemName.text = request.get(Request.ITEM_NAME).toString()
        requestItemCurr.text = request.get(Request.ITEM_CURRENT_QUANTITY).toString()
        requestItemDesired.text = request.get(Request.ITEM_MAX_QUANTITY).toString()

        return listViewItem
    }
}


