package com.example.nbathras.foodpantry

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

//Customized adapter for DistributorPageRequestActivity
class RequestList(val context:Activity, var requests: List<Request>):
    ArrayAdapter<Request>(context,R.layout.request_list_item, requests){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflating donor_splash_list_item layout
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.request_list_item, null, true)

        val textViewInventoryItem = listViewItem.findViewById<View>(R.id.requestItemName) as TextView
       // val textViewInventoryQuantity = listViewItem.findViewById<View>(R.id.requestQuantity) as TextView
        val textViewFinishedDate = listViewItem.findViewById<View>(R.id.requestFinishDate) as TextView
//
        val request = requests[position]
        //Populating 'name' and 'about' text fields for each list element
        var itemDescription = "";


//        for(item in request.itemsList) {
//            itemDescription +=  "Item Name: " + item.first + "Current Quantity:" + item.second.first + "Max Quantity" + item.second.second +  "\n"
//        }
        textViewInventoryItem.text =itemDescription
        //textViewInventoryQuantity.text = inventory.requestQuantity
        textViewFinishedDate.text = request.finishDate

        return listViewItem
    }
}


