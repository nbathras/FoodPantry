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

        val textViewRequestItemNameV = listViewItem.findViewById<View>(R.id.requestItemName) as TextView
        val requestIdAndUserIdV = listViewItem.findViewById<View>(R.id.requestIdAndUserId) as TextView
        val requestFinishDate = listViewItem.findViewById<View>(R.id.requestFinishDate) as TextView

        val request = requests[position]
        //Populating request text fields for each list element
        var itemDescription = "Item Description" + "\n"

        return listViewItem
    }
}


