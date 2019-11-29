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
class InventoryRequestList(val context:Activity, var inventories: List<Inventory>):
    ArrayAdapter<Inventory>(context,R.layout.inventory_list_item, inventories){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflating donor_splash_list_item layout
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.inventory_list_item, null, true)

        val textViewInventoryItem = listViewItem.findViewById<View>(R.id.inventoryItem) as TextView
        val textViewInventoryQuantity = listViewItem.findViewById<View>(R.id.inventoryQuantity) as TextView
        val textViewInventoryDropOff = listViewItem.findViewById<View>(R.id.inventoryDropOff) as TextView

        val inventory = inventories[position]
        //Populating 'name' and 'about' text fields for each list element
        textViewInventoryItem.text = inventory.requestItem
        textViewInventoryQuantity.text = inventory.requestQuantity
        textViewInventoryDropOff.text = inventory.requestDropOff

        return listViewItem
    }
}


