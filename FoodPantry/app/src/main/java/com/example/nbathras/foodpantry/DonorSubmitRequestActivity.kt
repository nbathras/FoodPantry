package com.example.nbathras.foodpantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.database.*
import java.io.Serializable

class DonorSubmitRequestActivity : AppCompatActivity() {

    private lateinit var distributorName: TextView
    private lateinit var donationToDistributorListView: ListView
    private lateinit var requestItemsList: MutableList<Pair<String, Pair<Int, Int>>>
    private lateinit var databaseDistributorRequestItems: DatabaseReference
    private lateinit var donationToDistributorList: DistributorRequestList
    private lateinit var donationToDistributorListAdapter: DonationToDistributorListItem
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_submit_request)

        val requestId = intent.getStringExtra(DistributorPageRequestActivity.REQUEST_ID)
        val distributorId = intent.getStringExtra(DistributorPageRequestActivity.DISTRIBUTOR_ID)
        intent.getStringExtra(DistributorPageRequestActivity.REQUEST_ITEMS)

        distributorName = findViewById(R.id.DonationLabelTextView)
        donationToDistributorListView = findViewById(R.id.RequestListView)
        submitButton = findViewById(R.id.SubmitButton)
        requestItemsList = intent.getSerializableExtra(DistributorPageRequestActivity.REQUEST_ITEMS)
                as MutableList<Pair<String, Pair<Int, Int>>>

    }
    override fun onStart() {
        super.onStart()
        }

    }


