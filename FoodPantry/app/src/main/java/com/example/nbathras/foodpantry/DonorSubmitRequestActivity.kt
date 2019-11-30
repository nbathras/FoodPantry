package com.example.nbathras.foodpantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.io.Serializable
import java.util.ArrayList
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
class DonorSubmitRequestActivity : AppCompatActivity() {

    private lateinit var distributorName: TextView
    private lateinit var donationToDistributorListView: ListView
    private lateinit var requestItemsList: ArrayList<Pair<String, Pair<Int, Int>>>
    private lateinit var donationToDistributorListAdapter: DonationToDistributorListItem
    private lateinit var submitButton: Button
    private lateinit var donationDateTitle: TextView
    private lateinit var seekBarValueMap: HashMap<String, Int>
    private lateinit var databaseDonations: DatabaseReference
    private lateinit var databaseDistributorRequest: DatabaseReference
    private lateinit var userID: String
    private lateinit var finishDate:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_submit_request)

        userID = intent.getStringExtra(DistributorPageRequestActivity.USER_ID)
        finishDate = intent.getStringExtra(DistributorPageRequestActivity.REQUEST_DATE)

        val requestId = intent.getStringExtra(DistributorPageRequestActivity.REQUEST_ID)
        val distributorId = intent.getStringExtra(DistributorPageRequestActivity.DISTRIBUTOR_ID)
        intent.getStringExtra(DistributorPageRequestActivity.REQUEST_ITEMS)

        databaseDonations = FirebaseDatabase.getInstance().getReference("donations").child(userID)
        databaseDistributorRequest = FirebaseDatabase.getInstance().getReference("requests").
            child(intent.getStringExtra(DistributorPageRequestActivity.DISTRIBUTOR_ID))

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
            submitDonation(seekBarValueMap)
        }

    }
    override fun onStart() {
        super.onStart()
        donationToDistributorListAdapter = DonationToDistributorListItem(this@DonorSubmitRequestActivity,
            requestItemsList)
        donationToDistributorListView.adapter = donationToDistributorListAdapter
        }

    fun submitDonation(seekBarMap:HashMap<String, Int>) {
        val donationList: ArrayList<Pair<String, Int>> = ArrayList<Pair<String, Int>>()
        val isDelivered: Boolean = false
        for((key, value) in seekBarMap){
            if(value != 0) {
                donationList.add(Pair(key, value))
            }
        }

        val randomId = databaseDonations.push().key
        val donation = Donation(randomId!!, userID,finishDate, isDelivered, donationList)

        databaseDonations.child(randomId).setValue(donation)

        Toast.makeText(this, "You have successfully submitted a donation!" +
                "Please arrive on the scheduled day.", Toast.LENGTH_LONG).show()
    }

    }


