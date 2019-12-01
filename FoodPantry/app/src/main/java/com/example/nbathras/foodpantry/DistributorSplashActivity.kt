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
    private lateinit var sharedPreferences: SharedPreferences
    var requestsDatabase: DatabaseReference? = null
    private lateinit var persistedRequests: MutableList<Request>
    private lateinit var listViewInventory: ListView
    private lateinit var listViewAdapter: ArrayAdapter<Request>
    private var shouldDelete: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_splash)
        //getting the reference of artists node
        requestsDatabase = FirebaseDatabase.getInstance().getReference("requests")

        sharedPreferences = getSharedPreferences(DonorSplashActivity.MY_PREFERENCE, Context.MODE_PRIVATE)
        // Clearing all previous requests




        persistedRequests = ArrayList()

        //List view defined in layout file
        listViewInventory = findViewById<View>(R.id.request_list) as ListView

        //When the user clicks on a specific distributor list item, show their biography/request page
        listViewInventory.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val distributor = persistedRequests[i]
                val intent = Intent(applicationContext, RequestList::class.java)
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
        requestsDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous distributors list
                persistedRequests.clear()

                //populating the distributors array from the database
                for (postSnapshot in dataSnapshot.children) {

                    var requestId = postSnapshot.child("requestId").value as String
                    var userId = postSnapshot.child("userId").value as String
                    var finisedDate = postSnapshot.child("finishDate").value as String
                    var itemsList = postSnapshot.child("itemsList").value as  ArrayList<Pair<String, Pair<Int, Int>>>
                    //  val persistedRequest = postSnapshot.getValue<Request>(Request::class.java)

                    val persistedRequest = Request(userId = userId, requestId = requestId, finishDate = finisedDate, itemsList = itemsList)
                    //add distributor to the list
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

    }
}
