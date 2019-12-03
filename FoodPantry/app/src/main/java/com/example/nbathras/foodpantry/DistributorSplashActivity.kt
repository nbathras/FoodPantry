package com.example.nbathras.foodpantry

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*

class DistributorSplashActivity : AppCompatActivity() {

    private var mAddRequestButton : Button? = null
    private lateinit var mDistributorName: TextView
    private lateinit var mDistributorAddress: TextView
    // private lateinit var mDistributorAdditionalAddress: TextView
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
    private lateinit var distributorAddressLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_splash)
        //getting the reference of artists node

        initializeUI()

        distributorsDatabase = FirebaseDatabase.getInstance().getReference("distributors")

        sharedPreferences = getSharedPreferences(DistributorSplashActivity.MY_PREFERENCE, Context.MODE_PRIVATE)
        if(sharedPreferences != null) {
            mDistributorID = sharedPreferences.getString(LoginActivity.USER_DISTRIBUTOR_ID,"").toString()

            if(mDistributorID != null) {
                requestsDatabase = FirebaseDatabase.getInstance().getReference("requests").child(mDistributorID)
            }
        }

        getDistributorValues()

        persistedRequests = ArrayList()

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

        // look at the login activity
        // all of the basic information like whether the user is a donor or distributor as well as
        // the email, name, address, about are all stored in shared preferences so we do not have to
        // continue doing gets every time we want to display the user profile
        // System will probably change in soon when I come up with a better way to store everything
    }

    private fun getDistributorValues() {
        distributorsDatabase!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //populating the distributors array from the database
                for (postSnapshot in dataSnapshot.children) {
                    for(postSnapshot2 in postSnapshot.children) {
                        if(postSnapshot2.key == mDistributorID) {
                            val distributor = postSnapshot2.getValue<Distributor>(Distributor::class.java)

                            if (distributor != null) {
                                mDistributorName.text = "Name: " + distributor.distributorName
                                mDistributorAbout.text = "About: " + distributor.distributorAbout
                                var distributorAddressArraylist = distributor.distributorLocation

                                var count = 1
                                for (address : String in distributorAddressArraylist) {
                                    // creates linear layout
                                    val linearLayout = LinearLayout(this@DistributorSplashActivity)
                                    linearLayout.layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                    linearLayout.orientation = LinearLayout.HORIZONTAL

                                    // creates text view containing the counter
                                    val text_view_count = TextView(this@DistributorSplashActivity)
                                    text_view_count.layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                    text_view_count.setText(count.toString() + ". ")

                                    // creatse text view containing the adress
                                    val text_view_address = TextView(this@DistributorSplashActivity)
                                    text_view_address.layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                    text_view_address.setText(address)
                                    text_view_address.setOnClickListener {
                                        onClickTextView(text_view_address)
                                    }
                                    text_view_address.setTextColor(Color.parseColor("#0000EE"))
                                    text_view_address.paintFlags = Paint.UNDERLINE_TEXT_FLAG

                                    // adds all of the views together
                                    linearLayout.addView(text_view_count)
                                    linearLayout.addView(text_view_address)
                                    distributorAddressLayout.addView(linearLayout)

                                    // increases address count
                                    count += 1
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                //Empty
            }
        })
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
                listViewAdapter = DistributorRequestList(this@DistributorSplashActivity, persistedRequests)
                listViewInventory.adapter = listViewAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                //Empty
            }
        })
    }

    private fun onClickTextView(v : TextView) {
        // Create a Uri from an intent string. Use the result to create an Intent.
        // val gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988")
        val text_view = v as TextView
        val address = text_view.text.toString()
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + address)

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent)
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
        listViewInventory = findViewById(R.id.request_list)
        distributorAddressLayout = findViewById(R.id.addressLayout)
        // mDistributorAdditionalAddress = findViewById(R.id.distributorLocationLable)
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
