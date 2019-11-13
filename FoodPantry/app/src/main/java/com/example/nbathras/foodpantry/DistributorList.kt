package com.example.nbathras.foodpantry

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

//Customized adapter for DonorSplashActivity
class DistributorList(private val context:Activity, internal var distributors: List<Distributor>):
    ArrayAdapter<Distributor>(context,R.layout.donor_splash_list_item, distributors){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflating donor_splash_list_item layout
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.donor_splash_list_item, null, true)

        val textViewName = listViewItem.findViewById<View>(R.id.distributorName) as TextView
        val textViewAbout = listViewItem.findViewById<View>(R.id.distributorAbout) as TextView

        val distributor = distributors[position]
        //Populating 'name' and 'about' text fields for each list element
        textViewName.text = distributor.distributorName
        textViewAbout.text = distributor.distributorAbout

        return listViewItem
    }
}
