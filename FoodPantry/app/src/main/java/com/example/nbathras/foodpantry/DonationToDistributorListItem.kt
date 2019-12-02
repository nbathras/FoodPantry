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
class DonationToDistributorListItem(private val context: Activity, var itemsList:  ArrayList<HashMap<String,Any>>):
    ArrayAdapter<HashMap<String,Any>>(context,R.layout.activity_donation_to_distributor_list_item, itemsList) {

    var seekBarValues: HashMap<String, Int> = HashMap<String, Int>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflating donor_splash_list_item layout
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.activity_donation_to_distributor_list_item, null, true)

        val requestItemName = listViewItem.findViewById<TextView>(R.id.requestItemFullName)
        val seekBarMinimum = listViewItem.findViewById<TextView>(R.id.seekBarMin)
        val seekBarMax = listViewItem.findViewById<TextView>(R.id.seekBarMax)
        val seekBar = listViewItem.findViewById<SeekBar>(R.id.donationQuantitySeekBar)
        val seekBarValueText = listViewItem.findViewById<TextView>(R.id.seekBarValue)

        val request = itemsList[position]

        requestItemName.text = request.get(Request.ITEM_NAME).toString()

        seekBarMinimum.text = "0"
        seekBarMax.text = (request.get(Request.ITEM_MAX_QUANTITY).toString().toInt() -
                request.get(Request.ITEM_CURRENT_QUANTITY).toString().toInt()).toString()

        seekBar.max = request.get(Request.ITEM_MAX_QUANTITY).toString().toInt() -
                request.get(Request.ITEM_CURRENT_QUANTITY).toString().toInt()
        seekBar.min = 0
        seekBar.setClickable(false)
        seekBar.setFocusable(false)
        seekBar.setEnabled(true)

        seekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {

                seekBarValues.put(request.get(Request.ITEM_NAME).toString(),progress)
                seekBarValueText.text = "quantity: " + progress.toString()
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
