package com.example.nbathras.foodpantry

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import kotlinx.android.synthetic.main.activity_distributor_page_request.*
import org.w3c.dom.Text


class DistributorPageRequestActivity : AppCompatActivity() {

    private lateinit var distributorName: TextView
    private lateinit var distributorAddressLayout: LinearLayout
    private lateinit var distributorAbout: TextView
    private lateinit var distributorPicture: ImageView
    private lateinit var listViewDistributorRequest: ListView
    private lateinit var distributorRequest: MutableList<Request>
    private lateinit var databaseDistributorRequest: DatabaseReference
    private lateinit var listViewRequestAdapter: DistributorRequestList
    private lateinit var distributorAddressArraylist: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_page_request)

        //Linking to XML layout (activity_distributor_page_request)
        distributorName = findViewById(R.id.distributorName)
        distributorAbout = findViewById(R.id.distributorAbout)
        distributorPicture = findViewById(R.id.distributor_picture)
        distributorAddressLayout = findViewById(R.id.addressLayout)
        listViewDistributorRequest = findViewById<View>(R.id.distributor_request_list) as ListView

        distributorAddressArraylist = intent.getStringArrayListExtra(DISTRIBUTOR_ADDRESS)

        //Setting distributor name text field
        distributorName.text = intent.getStringExtra(DISTRIBUTOR_NAME)
        //Setting distributor about text field
        distributorAbout.text = intent.getStringExtra(DISTRIBUTOR_ABOUT)
        //Setting distributor location text field
        var count = 1
        for (address : String in distributorAddressArraylist) {
            // creates linear layout
            val linearLayout = LinearLayout(this)
            linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            linearLayout.orientation = LinearLayout.HORIZONTAL

            // creates text view containing the counter
            val text_view_count = TextView(this)
            text_view_count.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            text_view_count.setText(count.toString() + ". ")

            // creatse text view containing the adress
            val text_view_address = TextView(this)
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

        distributorRequest = ArrayList()

        databaseDistributorRequest = FirebaseDatabase.getInstance().getReference("requests").
            child(intent.getStringExtra(DISTRIBUTOR_ID))

        val distributorID = intent.getStringExtra(DISTRIBUTOR_ID)
        val userID = intent.getStringExtra(USER_ID)
        val distributorName = intent.getStringExtra(DISTRIBUTOR_NAME)


        //Setting Distributor Profile Picture
        val imageIndex = intent.getIntExtra(IMAGE_INDEX, 0)
        val imageArray = arrayOf(R.drawable.profilepicture1, R.drawable.profilepic2, R.drawable.profilepic3)
        distributorPicture.setImageResource(imageArray[imageIndex])


        listViewDistributorRequest.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->

                val request = distributorRequest[i]
                val intent = Intent(applicationContext,DonorSubmitRequestActivity::class.java)
                intent.putExtra(DISTRIBUTOR_ID, distributorID)
                intent.putExtra(REQUEST_ID, request.requestId)
                intent.putExtra(REQUEST_ITEMS, request.itemsList)
                intent.putExtra(REQUEST_DATE, request.finishDate)
                intent.putExtra(USER_ID,userID)
                startActivity(intent)
            }
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

    companion object {
        const val USER_ID         = "userID"
        const val DISTRIBUTOR_NAME  = "distributorName"
        const val DISTRIBUTOR_ABOUT = "distributorAbout"
        const val DISTRIBUTOR_ID = "distributorID"
        const val REQUEST_ID = "requestID"
        const val REQUEST_ITEMS = "requestItems"
        const val REQUEST_DATE = "requestDate"
        const val DISTRIBUTOR_ADDRESS = "distributorAddress"
        const val IMAGE_INDEX = "imageIndex"
        private val TAG = "DistributorPageRequestActivity"

    }

}
