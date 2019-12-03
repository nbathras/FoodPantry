package com.example.nbathras.foodpantry

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_donor_submit_request.*

class DistributorRequestsDonorOverviewActivity : AppCompatActivity() {

    private lateinit var currRequest: Request
    var donationDatabase: DatabaseReference? = null
    private lateinit var listViewRequestAdapter: RequestList
    private lateinit var listViewDonorAdapter: DistributorRequestList
    private lateinit var listViewDonors: ListView
    private lateinit var listViewRequests: ListView
    private lateinit var listViewAdapter: ArrayAdapter<Donation>
    var requestsDatabase: DatabaseReference? = null
    private lateinit var requestItemsList: java.util.ArrayList<HashMap<String, Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_requests_donor_overview)
        //getting the reference of artists node

        donationDatabase = FirebaseDatabase.getInstance().getReference("donations")

        requestItemsList = intent.getSerializableExtra(DistributorPageRequestActivity.REQUEST_ITEMS)
                as java.util.ArrayList<HashMap<String, Any>>

        //List view defined in layout file
        listViewDonors = findViewById<View>(R.id.donor_list) as ListView
        listViewRequests = findViewById<View>(R.id.request_list) as ListView
            }

    override fun onStart() {
        super.onStart()

        //REQUESTS DATABASE
        requestsDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous distributors list
                currRequest = Request()

                //populating the distributors array from the database
                    val persistedRequest = dataSnapshot.getValue<Request>(Request::class.java)
                    currRequest = persistedRequest!!

                //Initializing listViewAdapter to customized DistributorList adapter
                listViewRequestAdapter = RequestList(
                    this@DistributorRequestsDonorOverviewActivity, requestItemsList)
                listViewRequests.adapter = listViewRequestAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                //Empty
            }
        })
    }

    companion object {
        private val TAG = "DistributorSplashActivity"
        const val MY_PREFERENCE   = "myPreference"
        const val USER_ID         = "userID"
        const val DISTRIBUTOR_NAME  = "distributorName"
        const val DISTRIBUTOR_ABOUT = "distributorAbout"
        const val DISTRIBUTOR_ADDRESS = "distributorAddress"
        const val USER_DISTRIBUTOR_PIC   = "userDistributorPic"
        const val DISTRIBUTOR_ID = "distributorID"
        const val DONOR_NAME = "donorName"
        const val REQUEST_ITEMS = "requestItems"
    }
}
