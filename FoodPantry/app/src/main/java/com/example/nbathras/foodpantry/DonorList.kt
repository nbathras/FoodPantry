package com.example.nbathras.foodpantry


import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

//Customized adapter for DistributorRequestsDonorActivity
class DonorList(val context:Activity, var donors: List<Donor>):
    ArrayAdapter<Donor>(context,R.layout.request_list_item, donors){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflating donor_item layout
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.donor_item, null, true)

        val textViewDonorNameV = listViewItem.findViewById<View>(R.id.donorRequestName) as TextView


        val donor = donors[position]
        //Populating request text field with donor name

        textViewDonorNameV.text = "Donor Name: " + donor.donorName +  "\n"

        return listViewItem
    }
}


