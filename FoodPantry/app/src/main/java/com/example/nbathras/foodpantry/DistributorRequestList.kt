package com.example.nbathras.foodpantry

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

//Customized adapter for DistributorPageRequestActivity
class DistributorRequestList(private val context:Activity, var requests: List<Request>):
    ArrayAdapter<Request>(context,R.layout.activity_distributor_request_list, requests) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflating distributor_splash_list_item layout
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.activity_distributor_request_list, null, true)

        val requestFinishDateText = listViewItem.findViewById<TextView>(R.id.requestMinQuantity)

        val request = requests[position]
        requestFinishDateText.text = request.finishDate.toString()
        requestFinishDateText.textSize = 20.0f
        requestFinishDateText.gravity = Gravity.CENTER

        return listViewItem

//        public View getView(...){
//            View v;
//            v.setOnClickListener(new OnClickListener() {
//                void onClick() {
//                    Intent intent= new Intent(context, ToActivity.class);
//                    intent.putExtra("your_extra","your_class_value");
//                    context.startActivity(intent);
//                }
//            });
//        }
    }


}
