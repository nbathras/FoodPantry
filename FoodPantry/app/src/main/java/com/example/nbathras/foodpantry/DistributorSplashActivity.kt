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

class DistributorSplashActivity : AppCompatActivity() {

    private var mAddRequestButton : Button? = null
    private lateinit var mDistributorName: TextView
    private lateinit var mDistributorAddress: TextView
    private lateinit var mDistributorAdditionalAddress: TextView
    private lateinit var mDistributorAbout: TextView

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mDistributor: Distributor
    private lateinit var mDistributorID: String
    var requestsDatabase: DatabaseReference? = null
    var distributorsDatabase: DatabaseReference? = null
    private lateinit var persistedRequests: MutableList<Request>
    private lateinit var listViewInventory: ListView
    private lateinit var listViewAdapter: ArrayAdapter<Request>
    private var shouldDelete: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_splash)
        //getting the reference of artists node

        distributorsDatabase = FirebaseDatabase.getInstance().getReference("distributors")

        sharedPreferences = getSharedPreferences(DistributorSplashActivity.MY_PREFERENCE, Context.MODE_PRIVATE)
        if(sharedPreferences != null) {
            mDistributorID = sharedPreferences.getString(LoginActivity.USER_DISTRIBUTOR_ID,"").toString()

            if(mDistributorID != null) {
                requestsDatabase = FirebaseDatabase.getInstance().getReference("requests").child(mDistributorID)
            }
        }

        persistedRequests = ArrayList()

        listViewInventory = findViewById(R.id.request_list)

        //When the user clicks on a specific distributor list item, show their biography/request page
        listViewInventory.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val request = persistedRequests[i]
                val intent = Intent(applicationContext, DistributorRequestsDonorOverviewActivity::class.java)
                intent.putExtra(DISTRIBUTOR_ID, mDistributorID)
                intent.putExtra(REQUEST_DATE, request.finishDate)
                intent.putExtra(REQUEST_ID, request.requestId)
                intent.putExtra(REQUEST_ITEMS, request.itemsList)
                startActivity(intent)
            }
        initializeUI()

        // look at the login activity
        // all of the basic information like whether the user is a donor or distributor as well as
        // the email, name, address, about are all stored in shared preferences so we do not have to
        // continue doing gets every time we want to display the user profile
        // System will probably change in soon when I come up with a better way to store everything
    }

    override fun onStart() {
        super.onStart()

        //DISTRIBUTORS DATABASE
        distributorsDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous distributors list
                persistedRequests.clear()

                //populating the distributors array from the database
                for (distributorSnapshot in dataSnapshot.children) {

                    for(distributor in distributorSnapshot.children) {

                        if(distributor.key == mDistributorID) {
                            val name = distributor.child("distributorName").getValue() as String
                            val about = distributor.child("distributorAbout").getValue() as String
                            val location = distributor.child("distributorLocation").getValue() as ArrayList<String>

                            mDistributor = Distributor(distributorAbout = about, distributorName = name, distributorLocation = location)

                        }
                    }
                }


                //Initializing listViewAdapter to customized DistributorRequestList adapter
                listViewAdapter = DistributorRequestList(this@DistributorSplashActivity, persistedRequests)
                listViewInventory.adapter = listViewAdapter

                if(mDistributor != null) {
                    mDistributorName.text = "Name: " + mDistributor.distributorName
                    mDistributorAbout.text = "About: " + mDistributor.distributorAbout
                    mDistributorAdditionalAddress.text = "Address: " + mDistributor.distributorLocation.toString()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                //Empty
            }
        })

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
                listViewAdapter = RequestList(this@DistributorSplashActivity, persistedRequests)
                listViewInventory.adapter = listViewAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                //Empty
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logged_in_menu, menu)



        mAddRequestButton!!.setOnClickListener {
           // val intent = Intent(this@DistributorSplashActivity, DonorSubmitRequestActivity::class.java)
            val intent = Intent(this@DistributorSplashActivity, AddDistributorRequestActivity::class.java)
            startActivity(intent)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.loggedInMenu_logOutItem) {
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent);
            return true
        }
        if (item.itemId == R.id.loggedInMenu_editProfileItem) {
            val intent = Intent(this, DistributorEditActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun initializeUI() {
        mAddRequestButton = findViewById(R.id.activityDistributorSplash_addRequestButton)
        mDistributorName = findViewById(R.id.distributorNameLable)
        mDistributorAbout = findViewById(R.id.distributorAboutLable)
        mDistributorAdditionalAddress = findViewById(R.id.distributorLocationLable)

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
        const val REQUEST_DATE = "requestDate"
        const val REQUEST_ID = "requestId"
        const val REQUEST_ITEMS = "requestItems"

    }
}
