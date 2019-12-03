package com.example.nbathras.foodpantry

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.io.Serializable
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.HashMap
import java.text.SimpleDateFormat
import android.widget.Toast



class DonorSubmitRequestActivity : AppCompatActivity() {

    private lateinit var distributorName: TextView
    private lateinit var donationToDistributorListView: ListView
    private lateinit var requestItemsList: ArrayList<HashMap<String,Any>>
    private lateinit var donationToDistributorListAdapter: DonationToDistributorListItem
    private lateinit var submitButton: Button
    private lateinit var addDeliveryDateButton: Button
    private lateinit var donationDateTitle: TextView
    private lateinit var seekBarValueMap: HashMap<String, Int>
    private lateinit var databaseDonations: DatabaseReference
    private lateinit var databaseDistributorRequest: DatabaseReference
    private lateinit var databaseCorrespondingRequest: DatabaseReference
    private lateinit var userID: String
    private lateinit var finishDate:String
    private lateinit var requestId: String
    private lateinit var distributorId: String
    private  var donationList: ArrayList<Pair<String, Int>> = ArrayList<Pair<String, Int>>()
    private  var donationHashMap: HashMap<String, Int> = HashMap<String, Int>()
    private var currentRequest: Request = Request()
    private var cal : Calendar = Calendar.getInstance()
    private var deliveryDate: String = "00/00/0000"



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

        databaseCorrespondingRequest = databaseDistributorRequest.child(requestId)

        distributorName = findViewById(R.id.DonationLabelTextView)
        donationToDistributorListView = findViewById(R.id.RequestListView)
        submitButton = findViewById(R.id.SubmitButton)
        addDeliveryDateButton = findViewById(R.id.addDeliveryDateButton)
        requestItemsList = intent.getSerializableExtra(DistributorPageRequestActivity.REQUEST_ITEMS)
                as  ArrayList<HashMap<String,Any>>

        donationDateTitle = findViewById(R.id.DonationLabelTextView)
        donationDateTitle.text = intent.getStringExtra(DistributorPageRequestActivity.REQUEST_DATE)

        val dateSetListener = object: DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
                val myFormat = "MM/dd/yyyy" // mention the format you need
                val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
                deliveryDate = simpleDateFormat.format(cal.getTime())
            }
        }

        addDeliveryDateButton.setOnClickListener {
            DatePickerDialog(this@DonorSubmitRequestActivity, dateSetListener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

    }
    override fun onStart() {
        super.onStart()
        databaseCorrespondingRequest.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                currentRequest = Request()

                val request = dataSnapshot.getValue<Request>(Request::class.java)
                currentRequest = request!!

                donationToDistributorListAdapter = DonationToDistributorListItem(this@DonorSubmitRequestActivity,
                requestItemsList)
                 donationToDistributorListView.adapter = donationToDistributorListAdapter
                seekBarValueMap = donationToDistributorListAdapter.seekBarValues

                submitButton.setOnClickListener {
                    //submit donation from seek bar values
                    if(deliveryDate != "00/00/0000") {
                        submitDonation(seekBarValueMap)
                        //update the request list
                        updateRequestList()

                        var newIntent =  Intent(applicationContext, DonorSplashActivity::class.java)
                        startActivity(newIntent)
                    } else {
                        Toast.makeText(
                            this@DonorSubmitRequestActivity,
                            "Please select a delivery date before submitting.",
                            Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {
                //Empty
            }
        })
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
        //New request list to be populated with updated values for corresponding request
        var updatedRequestList: ArrayList<HashMap<String,Any>> = ArrayList<HashMap<String,Any>>()

        for(i in currentRequest.itemsList){
            var currItemName = i.get(Request.ITEM_NAME) as String
            var currQuantity = i.get(Request.ITEM_CURRENT_QUANTITY).toString().toInt()
            var maxQuantity = i.get(Request.ITEM_MAX_QUANTITY).toString().toInt()
            var item = HashMap<String,Any>()
            //Checks if any donations have been made for a particular item in the current request list. If so, update new values in new request list
            if(donationHashMap.containsKey(currItemName)) {
                var newCurrentQuantity = currQuantity + (donationHashMap.get(currItemName)!!)
                item.put(Request.ITEM_CURRENT_QUANTITY, newCurrentQuantity)
            }
            //Else, add originally value to updatedRequestList
            else {
                item.put(Request.ITEM_CURRENT_QUANTITY, currQuantity!!)
            }

            item.put(Request.ITEM_NAME, currItemName)
            item.put(Request.ITEM_MAX_QUANTITY, maxQuantity!!)
            updatedRequestList.add(item)
        }
        var newUpdatedRequest = Request(currentRequest.deliveryDate, currentRequest.requestId, currentRequest.finishDate, updatedRequestList)
        databaseDistributorRequest.child(requestId).setValue(newUpdatedRequest)
    }

    }


