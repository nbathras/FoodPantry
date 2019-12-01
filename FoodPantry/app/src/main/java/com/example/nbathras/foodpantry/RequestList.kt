package com.example.nbathras.foodpantry

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

//Customized adapter for DistributorSplashActivity
class RequestList(val context:Activity, var requests: List<Request>):
    ArrayAdapter<Request>(context,R.layout.request_list_item, requests){

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


        // prepare item description string
        for(item in request.itemsList) {
            itemDescription +=  "Item Name: " + item[Request.ITEM_NAME] + "\n" + "Current Quantity:" + item[Request.ITEM_CURRENT_QUANTITY] +"\n" + "MAX Quantity:" + item[Request.ITEM_MAX_QUANTITY] +"\n"
        }
        textViewRequestItemNameV.setText(itemDescription)
        requestIdAndUserIdV.text = "Request Id: " + request.requestId +  "\n"  +" UserId: " + request.userId
        requestFinishDate.text = "Finish Date: " +request.finishDate

        return listViewItem
    }
}


