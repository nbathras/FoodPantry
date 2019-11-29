package com.example.nbathras.foodpantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.io.Serializable
import java.util.ArrayList

class DonorSubmitRequestActivity : AppCompatActivity() {

    private lateinit var distributorName: TextView
    private lateinit var donationToDistributorListView: ListView
    private lateinit var requestItemsList: ArrayList<Pair<String, Pair<Int, Int>>>
    private lateinit var donationToDistributorListAdapter: DonationToDistributorListItem
    private lateinit var submitButton: Button
    private lateinit var donationDateTitle: TextView
    private lateinit var seekBarValueMap: HashMap<String, Int>


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
                as ArrayList<Pair<String, Pair<Int, Int>>>

        donationDateTitle = findViewById(R.id.DonationLabelTextView)
        donationDateTitle.text = intent.getStringExtra(DistributorPageRequestActivity.REQUEST_DATE)

        submitButton.setOnClickListener {
            seekBarValueMap = DonationToDistributorListItem(this@DonorSubmitRequestActivity,
                requestItemsList).seekBarValues
        }

    }
    override fun onStart() {
        super.onStart()
        donationToDistributorListAdapter = DonationToDistributorListItem(this@DonorSubmitRequestActivity,
            requestItemsList)
        donationToDistributorListView.adapter = donationToDistributorListAdapter
        }

    }


