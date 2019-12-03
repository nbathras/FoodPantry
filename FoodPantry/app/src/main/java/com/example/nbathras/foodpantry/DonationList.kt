package com.example.nbathras.foodpantry


import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.w3c.dom.Text

//Customized adapter for DistributorRequestsDonorActivity
class DonationList(val context:Activity, var donations: List<Donation>):
    ArrayAdapter<Donation>(context,R.layout.request_list_item, donations){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflating donor_item layout
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.donor_item, null, true)

        val donorRequestName = listViewItem.findViewById<TextView>(R.id.donorRequestName)
        val donorRequestDate = listViewItem.findViewById<TextView>(R.id.donorRequestDate)

        val donation = donations[position]

        donorRequestName.text = donation.userName.toString()
        donorRequestDate.text = donation.deliveryDate.toString()

        return listViewItem
    }
}


