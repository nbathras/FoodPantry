package com.example.nbathras.foodpantry

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.TextView
import org.w3c.dom.Text

//Customized adapter for DonorSubmitRequestActivity
class DonationToDistributorListItem(private val context: Activity, var itemsList: ArrayList<Pair<String, Pair<Int, Int>>>):
    ArrayAdapter<Pair<String, Pair<Int, Int>>>(context,R.layout.activity_donation_to_distributor_list_item, itemsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflating donor_splash_list_item layout
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.activity_distributor_request_list, null, true)

        val requestItemName = listViewItem.findViewById<TextView>(R.id.DonationLabelTextView)
        val seekBarMinimum = listViewItem.findViewById<TextView>(R.id.seekBarMin)
        val seekBarMax = listViewItem.findViewById<TextView>(R.id.seekBarMax)
        val seekBar = listViewItem.findViewById<SeekBar>(R.id.donationQuantitySeekBar)

        val request = itemsList[position]

        seekBarMinimum.text = "0"
        seekBarMax.text = request.second.second.toString()

        seekBar.max = request.second.second
        seekBar.min = 0
        seekBar.setClickable(false)
        seekBar.setFocusable(false)
        seekBar.setEnabled(false)

        seekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                // write custom code for progress is changed
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })


        return listViewItem
    }
}
