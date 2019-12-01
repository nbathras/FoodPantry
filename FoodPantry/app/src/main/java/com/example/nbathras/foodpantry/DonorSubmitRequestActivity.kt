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
    private lateinit var requestId: String
    private lateinit var distributorId: String
    private  var donationList: ArrayList<Pair<String, Int>> = ArrayList<Pair<String, Int>>()
    private  var donationHashMap: HashMap<String, Int> = HashMap<String, Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_submit_request)

        userID = intent.getStringExtra(DistributorPageRequestActivity.USER_ID)
        finishDate = intent.getStringExtra(DistributorPageRequestActivity.REQUEST_DATE)

        requestId = intent.getStringExtra(DistributorPageRequestActivity.REQUEST_ID)
        distributorId = intent.getStringExtra(DistributorPageRequestActivity.DISTRIBUTOR_ID)
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
            //submit donation from seek bar values
            submitDonation(seekBarValueMap)
            //update the request list
            updateRequestList()
        }

    }
    override fun onStart() {
        super.onStart()
        donationToDistributorListAdapter = DonationToDistributorListItem(this@DonorSubmitRequestActivity,
            requestItemsList)
        donationToDistributorListView.adapter = donationToDistributorListAdapter
        }

    //This function will submit the donation values to the database
    fun submitDonation(seekBarMap:HashMap<String, Int>) {
        var isDelivered: Boolean = false
        for((key, value) in seekBarMap){
            if(value != 0) {
                //Adding donation items to an arraylist (because it is used in Donation object)
                donationList.add(Pair(key, value))
                //Adding a donation item to hashmap  (because it is easier to access when updating Request)
                donationHashMap.put(key, value)
            }
        }
        //Create donation object
        val donation = Donation(requestId, userID,finishDate, isDelivered, donationList)

        databaseDonations.child(requestId).setValue(donation)

        Toast.makeText(this, "You have successfully submitted a donation!" +
                "Please arrive on the scheduled day.", Toast.LENGTH_LONG).show()
    }

    /**This function will be called in order to update the Request item values once a donation has been submitted
     * ex: if the orginially amount of cereal needed is 10 boxes, it will decrease to 8 once a donation is made for 2 boxes
     */
    fun updateRequestList() {
        //Retrieve request with corresponding requestID
//        var request = (databaseDistributorRequest.child(requestId)) as Request
//        //New request list to be populated with updated values for corresponding request
//        var updatedRequestList: ArrayList<Pair<String, Pair<Int, Int>>> = ArrayList<Pair<String, Pair<Int, Int>>>()
//
//        for((key, valueRequest) in request.itemsList){
//            //Checks if any donations have been made for a particular item in the current request list. If so, update new values in new request list
//            if(donationHashMap.containsKey(key)) {
//                var newNumNeeded = valueRequest.second - donationHashMap.getValue(key)
//                updatedRequestList.add(Pair(key, Pair(0, newNumNeeded)))
//            }
//            //Else, add originally value to updatedRequestList
//            else {
//                updatedRequestList.add(Pair(key, Pair(0, valueRequest.second)))
//            }
//        }
//        var newUpdatedRequest = Request(request.deliveryDate, request.requestId, request.finishDate, updatedRequestList)
//        databaseDistributorRequest.child(requestId).setValue(newUpdatedRequest)
    }

    }


