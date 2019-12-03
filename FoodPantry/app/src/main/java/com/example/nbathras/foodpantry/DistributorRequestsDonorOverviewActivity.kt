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

    private lateinit var mDonorName: TextView
    private lateinit var mDate: TextView
    private lateinit var donorItemsList: List<Donor>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mDonor: Donor
    private lateinit var mDonation: Donation
    private lateinit var mDonorID: String
    private lateinit var mDonationID: String
    var donationsDatabase: DatabaseReference? = null
    var donorsDatabase: DatabaseReference? = null
    var distributorsDatabase: DatabaseReference? = null
    private lateinit var listViewRequestAdapter: DistributorRequestList
    private lateinit var listViewDonorAdapter: DistributorRequestList
    private lateinit var persistedDonations: MutableList<Donation>
    private lateinit var persistedRequests: MutableList<Request>
    private lateinit var persistedDonors: MutableList<Donor>
    private lateinit var listViewDonors: ListView
    private lateinit var listViewRequests: ListView
    private lateinit var listViewAdapter: ArrayAdapter<Donation>
    private var mAddRequestButton : Button? = null
    private lateinit var mDistributorName: TextView
    // private lateinit var mDistributorAdditionalAddress: TextView
    private lateinit var mDistributorAbout: TextView
    private lateinit var mDistributor: Distributor
    var requestsDatabase: DatabaseReference? = null
    private lateinit var listViewInventory: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_requests_donor_overview)
        //getting the reference of artists node

        donorsDatabase = FirebaseDatabase.getInstance().getReference("donors")
        sharedPreferences = getSharedPreferences(DistributorRequestsDonorOverviewActivity.MY_PREFERENCE, Context.MODE_PRIVATE)
        if(sharedPreferences != null) {
            mDonorID = sharedPreferences.getString(LoginActivity.USER_DONOR_ID,"").toString()

            if(mDonorID != null) {
                donationsDatabase = FirebaseDatabase.getInstance().getReference("donations").child(mDonorID)
            }
        }

        persistedDonations = ArrayList()
        persistedRequests = ArrayList()
        persistedDonors = ArrayList()

        //List view defined in layout file
        listViewDonors = findViewById<View>(R.id.donor_list) as ListView
        listViewRequests = findViewById<View>(R.id.request_list) as ListView

        //When the user clicks on a  request or donor list view, show their adapters
        listViewRequests.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val request = persistedRequests[i]
                val intent = Intent(applicationContext, RequestList::class.java)
                intent.putExtra(DistributorRequestsDonorOverviewActivity.REQUEST_ITEMS, request.itemsList)
                startActivity(intent)
            }

        listViewDonors.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val donor = persistedDonors[i]
                val intent = Intent(applicationContext, DonorList::class.java)
                intent.putExtra(DistributorRequestsDonorOverviewActivity.DONOR_NAME, donor.donorName)
                startActivity(intent)
            }


        initializeUI()

            }

    override fun onStart() {
        super.onStart()

        //REQUESTS DATABASE
        requestsDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous distributors list
                persistedRequests.clear()

                //populating the distributors array from the database
                for (postSnapshot in dataSnapshot.children) {

                    val persistedRequest = postSnapshot.getValue<Request>(Request::class.java)
                    persistedRequests.add(persistedRequest!!)
                }

                //Initializing listViewAdapter to customized DistributorList adapter
                listViewRequestAdapter = DistributorRequestList(this@DistributorRequestsDonorOverviewActivity,
                    persistedRequests)
                listViewRequests.adapter = listViewRequestAdapter
            }
            override fun onCancelled(p0: DatabaseError) {
                //Empty
            }
        })

        //DONOR DATABASE - need to fix error happening at donorItemsList
//        donorsDatabase!!.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                //clearing the previous distributors list
//                persistedDonors.clear()
//
//                //populating the distributors array from the database
//
//                val persistedDonor = dataSnapshot.getValue<Donor>(Donor::class.java)
//
//                //Initializing listViewAdapter to customized DistributorList adapter
//                listViewDonorAdapter = DistributorRequestList(this@DistributorRequestsDonorOverviewActivity,
//                   donorItemsList)
//                listViewDonors.adapter = listViewDonorAdapter
//            }
//            override fun onCancelled(p0: DatabaseError) {
//                //Empty
//            }
//        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logged_in_menu, menu)

        return true
    }



    private fun initializeUI() {
        mAddRequestButton = findViewById(R.id.activityDistributorSplash_addRequestButton)
        mDistributorName = findViewById(R.id.distributorNameLable)
        mDistributorAbout = findViewById(R.id.distributorAboutLable)
       //  mDistributorAdditionalAddress = findViewById(R.id.distributorLocationLable)
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
