package com.example.nbathras.foodpantry

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddDistributorRequestActivity : AppCompatActivity() {
    private var parentLinearLayout: LinearLayout? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mAddRequestButton : Button
    private lateinit var mItemFinishDate : EditText
    private lateinit var mItemName : EditText
    private lateinit var mItemCurrentQuantity : EditText
    private lateinit var mItemMaxQuantity : EditText
    private lateinit var databaseDistributorRequest: DatabaseReference
    private lateinit var  itemsList : ArrayList<Pair<String, Pair<Int, Int>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_distributor_request);

        initializeUI()
        mAddRequestButton.setOnClickListener {

            // Create the Rquest Object
            var itemFinishDate = mItemFinishDate.text.toString()

            // adding request to database with items and new request id
            val requestId  = databaseDistributorRequest.child("10/10/2010").push().key

            if (requestId != null) {
                sharedPreferences = getSharedPreferences(DonorSplashActivity.MY_PREFERENCE, Context.MODE_PRIVATE)
                //Obtaining specific userID through sharedPreferences
                var userUID = sharedPreferences.getString(DonorSplashActivity.USER_ID, "Null").toString()
                val request = Request("",requestId,itemFinishDate,itemsList, userUID)
                databaseDistributorRequest.child(requestId).setValue(request)
            }

            // Navigate back to DistributorSplashActivity
            val intent = Intent(this@AddDistributorRequestActivity, DistributorSplashActivity::class.java)
            startActivity(intent)
        }

        itemsList = ArrayList()
        parentLinearLayout = findViewById(R.id.parent_linear_layout) as LinearLayout

        databaseDistributorRequest = FirebaseDatabase.getInstance().getReference("requests")
    }

    override fun onStart() {
        super.onStart()
        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.request_item, null)

        mItemName = rowView.findViewById(R.id.item_name)
        mItemCurrentQuantity = rowView.findViewById(R.id.item_current)
        mItemMaxQuantity = rowView.findViewById(R.id.item_max)
        // Add the new row before the add field button.
        parentLinearLayout!!.addView(rowView, parentLinearLayout!!.childCount - 2)
    }
    fun onAddField(v: View?) {

        // create item class
        val itemName =  mItemName.getText().toString()
        val itemCurrnetQuantity =  mItemCurrentQuantity.getText().toString().toInt()
        val itemMaxQuantity =  mItemMaxQuantity.getText().toString().toInt()

        val quantity = Pair(itemCurrnetQuantity,itemMaxQuantity);
        itemsList.add(Pair(itemName, quantity))


        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.request_item, null)

        // Bind Items Properties
        mItemName = rowView.findViewById(R.id.item_name)
        mItemCurrentQuantity = rowView.findViewById(R.id.item_current)
        mItemMaxQuantity = rowView.findViewById(R.id.item_max)
        // Add the new row before the add field button.
        parentLinearLayout!!.addView(rowView, parentLinearLayout!!.childCount - 1)
    }

    fun onDelete(v: View) {
        parentLinearLayout!!.removeView(v.parent as View)
    }


    private fun initializeUI() {

        mItemFinishDate = findViewById(R.id.item_finish_date)
        mAddRequestButton= findViewById(R.id.activityAddRequest_addRequestButton)


    }

}
