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
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.get
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddDistributorRequestActivity : AppCompatActivity() {
    private lateinit var itemFormLayout: LinearLayout

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mDistributorID: String
    private lateinit var mAddRequestButton : Button
    private lateinit var mAddItemButton : Button
    private lateinit var mItemFinishDate : EditText
    private lateinit var databaseDistributorRequest: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_distributor_request);

        //Initializes items list to store request names and quantities
        sharedPreferences = getSharedPreferences(DistributorSplashActivity.MY_PREFERENCE, Context.MODE_PRIVATE)

        initializeUI()

        mAddRequestButton.setOnClickListener {
            // Create the Request Object
            var itemsList =  ArrayList<HashMap<String,Any>>()
            var itemFinishDate = mItemFinishDate.text.toString()

            // adding request to database with items and new request id
            val requestId  = databaseDistributorRequest.push().key

            if (requestId != null) {
                for (nextView: View in itemFormLayout.children) {
                    var nextLinearLayout = nextView as LinearLayout
                    var itemNameEditView = (nextLinearLayout.get(0) as RelativeLayout).get(0) as EditText
                    var itemCurrentEditView = (nextLinearLayout.get(1) as RelativeLayout).get(0) as EditText
                    var itemMaxEditTExt = (nextLinearLayout.get(2) as RelativeLayout).get(0) as EditText

                    var entry = HashMap<String, Any>()
                    entry.put(Request.ITEM_NAME, itemNameEditView.text.toString())
                    entry.put(Request.ITEM_CURRENT_QUANTITY, itemCurrentEditView.text.toString())
                    entry.put(Request.ITEM_MAX_QUANTITY, itemMaxEditTExt.text.toString())
                    
                    itemsList.add(entry)
                }

                val request = Request("", requestId, itemFinishDate, itemsList, mDistributorID)
                // Binds user ID with request ID
                databaseDistributorRequest.child(requestId).setValue(request)
            }

            // Navigate back to DistributorSplashActivity
            val intent = Intent(this@AddDistributorRequestActivity, DistributorSplashActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAddItemButton.setOnClickListener { createItemForm() }

        if(sharedPreferences != null) {
            //Gets distributor ID
            mDistributorID = sharedPreferences.getString(LoginActivity.USER_DISTRIBUTOR_ID,"").toString()
            //Gets path with user ID
            databaseDistributorRequest = FirebaseDatabase.getInstance().getReference("requests").child(mDistributorID)
        }

    }

    override fun onStart() {
        super.onStart()
        createItemForm()
    }

    private fun createItemForm() {
        // create item class
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.request_item, null)

        // Add the new row before the add field button.
        itemFormLayout!!.addView(rowView)

//        Log.i("TEST", "Count: " + itemFormLayout.childCount)
//        for (nextView: View in itemFormLayout.children) {
//            var nextLinearLayout = nextView as LinearLayout
//            var test = nextLinearLayout.get(0) as EditText
//            Log.i("TEST", test.text.toString())
//        }
    }

    fun onDelete(v: View) {
        itemFormLayout!!.removeView(v.parent.parent as View)
    }


    private fun initializeUI() {
        mItemFinishDate = findViewById(R.id.item_finish_date)
        mAddRequestButton= findViewById(R.id.activityAddRequest_addRequestButton)
        itemFormLayout = findViewById(R.id.item_form_layout)
        mAddItemButton = findViewById(R.id.add_field_button)
    }

}
