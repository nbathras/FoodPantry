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

    var donationDatabase: DatabaseReference? = null
    var requestsDatabase: DatabaseReference? = null
    private lateinit var listViewRequestAdapter: RequestList
    private lateinit var listViewDonations: ListView
    private lateinit var listViewRequests: ListView
    private lateinit var listViewDonationAdapter: ArrayAdapter<Donation>
    private lateinit var requestItemsList: java.util.ArrayList<HashMap<String, Any>>
    private lateinit var donationList: MutableList<Donation>
    private lateinit var requestID: String
    private lateinit var currRequest: Request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_requests_donor_overview)
        //getting the reference of artists node

        donationDatabase = FirebaseDatabase.getInstance().getReference("donations")

        requestItemsList = intent.getSerializableExtra(DistributorPageRequestActivity.REQUEST_ITEMS)
                as java.util.ArrayList<HashMap<String, Any>>

        requestID = intent.getStringExtra(REQUEST_ID)!!

        //List view defined in layout file
        listViewDonations = findViewById<View>(R.id.donation_list) as ListView
        listViewRequests = findViewById<View>(R.id.item_list) as ListView
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

        donationDatabase!!.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                donationList.clear()
                for(postSnapshot in dataSnapshot.children){
                    if(postSnapshot.hasChild(requestID)) {
                        val donation = postSnapshot.child(requestID).getValue<Donation>(Donation::class.java)
                        donationList.add(donation!!)
                    }

                }

                listViewDonationAdapter = DonationList(this@DistributorRequestsDonorOverviewActivity,
                    donationList)
                listViewDonations.adapter = listViewDonationAdapter
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
        const val REQUEST_ID = "requestId"
    }
}
