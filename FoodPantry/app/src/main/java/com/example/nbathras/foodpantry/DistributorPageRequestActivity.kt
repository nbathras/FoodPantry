package com.example.nbathras.foodpantry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class DistributorPageRequestActivity : AppCompatActivity() {

    private lateinit var distributorName: TextView
    private lateinit var distributorAbout: TextView
    private lateinit var distributorPicture: ImageView
    private lateinit var listViewDistributorRequest: ListView
    private lateinit var distributorRequest: MutableList<Request>
    private lateinit var databaseDistributorRequest: DatabaseReference
    private lateinit var listViewRequestAdapter: DistributorRequestList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_page_request)

        //Linking to XML layout (activity_distributor_page_request)
        distributorName = findViewById(R.id.distributorName)
        distributorAbout = findViewById(R.id.distributorAbout)
        distributorPicture = findViewById(R.id.distributor_picture)
        listViewDistributorRequest = findViewById<View>(R.id.distributor_request_list) as ListView

        //Setting distributor name text field
        distributorName.text = intent.getStringExtra(DISTRIBUTOR_NAME)
        //Setting distributor about text field
        distributorAbout.text = intent.getStringExtra(DISTRIBUTOR_ABOUT)

        databaseDistributorRequest = FirebaseDatabase.getInstance().getReference("requests").
            child(intent.getStringExtra(DISTRIBUTOR_ID))
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
    }

}
