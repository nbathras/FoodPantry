package com.example.nbathras.foodpantry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class DistributorPageRequestActivity : AppCompatActivity() {

    private lateinit var distributorName: TextView
    private lateinit var distributorAddressLayout: LinearLayout
    private lateinit var distributorAbout: TextView
    private lateinit var distributorPicture: ImageView
    private lateinit var listViewDistributorRequest: ListView
    private lateinit var distributorRequest: MutableList<Request>
    private lateinit var databaseDistributorRequest: DatabaseReference
    private lateinit var listViewRequestAdapter: DistributorRequestList
    private lateinit var distributorAddressArraylist: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_page_request)

        //Linking to XML layout (activity_distributor_page_request)
        distributorName = findViewById(R.id.distributorName)
        distributorAbout = findViewById(R.id.distributorAbout)
        distributorPicture = findViewById(R.id.distributor_picture)
        distributorAddressLayout = findViewById(R.id.addressLayout)
        listViewDistributorRequest = findViewById<View>(R.id.distributor_request_list) as ListView

        distributorAddressArraylist = intent.getStringArrayListExtra(DISTRIBUTOR_ADDRESS)

        //Setting distributor name text field
        distributorName.text = intent.getStringExtra(DISTRIBUTOR_NAME)
        //Setting distributor about text field
        distributorAbout.text = intent.getStringExtra(DISTRIBUTOR_ABOUT)
        //Setting distributor location text field
        var count = 1
        for (address : String in distributorAddressArraylist) {
            val text_view = TextView(this)
            text_view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            text_view.setText(count.toString() + ". " + address)
            distributorAddressLayout.addView(text_view)
            count += 1
        }

        distributorRequest = ArrayList()

        databaseDistributorRequest = FirebaseDatabase.getInstance().getReference("requests").
            child(intent.getStringExtra(DISTRIBUTOR_ID))

        val distributorID = intent.getStringExtra(DISTRIBUTOR_ID)
        val userID = intent.getStringExtra(USER_ID)
        val distributorName = intent.getStringExtra(DISTRIBUTOR_NAME)


        listViewDistributorRequest.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->

                val request = distributorRequest[i]
                val intent = Intent(applicationContext,DonorSubmitRequestActivity::class.java)
                intent.putExtra(DISTRIBUTOR_ID, distributorID)
                intent.putExtra(REQUEST_ID, request.requestId)
                intent.putExtra(REQUEST_ITEMS, request.itemsList)
                intent.putExtra(REQUEST_DATE, request.finishDate)
                intent.putExtra(USER_ID,userID)
                startActivity(intent)
            }
    }

    override fun onStart() {
        super.onStart()
        databaseDistributorRequest.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous distributors list
                distributorRequest.clear()

                //populating the distributors array from the database
                for (postSnapshot in dataSnapshot.children) {
                    val request = postSnapshot.getValue<Request>(Request::class.java)
                    //add distributor to the list
                    distributorRequest.add(request!!)
                }

                //Initializing listViewAdapter to customized DistributorList adapter
                listViewRequestAdapter = DistributorRequestList(this@DistributorPageRequestActivity,
                    distributorRequest)
                listViewDistributorRequest.adapter = listViewRequestAdapter

            }

            override fun onCancelled(p0: DatabaseError) {
                //Empty
            }
        })
    }

        companion object {
        const val USER_ID         = "userID"
        const val DISTRIBUTOR_NAME  = "distributorName"
        const val DISTRIBUTOR_ABOUT = "distributorAbout"
        const val DISTRIBUTOR_ID = "distributorID"
        private val TAG = "DistributorPageRequestActivity"
            const val REQUEST_ID = "requestID"
            const val REQUEST_ITEMS = "requestItems"
            const val REQUEST_DATE = "requestDate"
            const val DISTRIBUTOR_ADDRESS = "distributorAddress"
    }

}
