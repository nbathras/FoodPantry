package com.example.nbathras.foodpantry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.widget.*
import com.google.firebase.database.*
import java.util.ArrayList


class RequestActivity : AppCompatActivity() {

    private lateinit var mItemEditText: EditText
    private lateinit var mQuantityDesiredEditText: EditText
    private lateinit var mCurrentQuantityEditText: EditText
    private lateinit var mFinishDateEditText: EditText
    private lateinit var requestItemsList: ArrayList<Pair<String, Pair<Int, Int>>>
    private lateinit var mAddRequestButton : Button
    var databaseRequests: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_request);
        databaseRequests = FirebaseDatabase.getInstance().getReference("inventories")
        initializeUI()

        mAddRequestButton.setOnClickListener {
            addRequest()
        }


    }


//    override fun onStart() {
//        super.onStart()
//
//        //getting the reference of artists node
//        databaseInventories = FirebaseDatabase.getInstance().getReference("inventories")
//
//
//
//        databaseInventories!!.addValueEventListener(object : ValueEventListener {
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//                    for (locationSnapshot in dataSnapshot.children) {
//                        val inventory = locationSnapshot.value!!.toString();
//
//                        Log.d("","Inventory: $inventory")
//                    }
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Failed to read value
//            }
//        })
//    }
    private fun addRequest() {
        val item: String    = mItemEditText.text.toString()
        val quantity: String = mQuantityDesiredEditText.text.toString()
        val dropOff: String     = mFinishDateEditText.text.toString()

        if (item.isEmpty()) {
            Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show()
            return
        }

        if (quantity.isEmpty() ) {
            Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show()
            return
        }

        if (dropOff.isEmpty()) {
            Toast.makeText(this, "Please enter drop off date", Toast.LENGTH_SHORT).show()
            return
        }

        // saves request to the database


        addRequestObjectToDatabase(item,quantity,dropOff)
        //launch login activity
        val intent = Intent(this@RequestActivity, DistributorSplashActivity::class.java)
        startActivity(intent)

    }

    private fun addRequestObjectToDatabase(item : String, quantity: String, dropOff: String) {
        //getting the values to save


        //checking if the value is provided
        if (!TextUtils.isEmpty(item)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            // val id = databaseInventories.push().getKey()
            val id = databaseRequests!!.push().key;
            //creating an Artist Object
            val inventory = Inventory(item, quantity, dropOff)

            //Saving the requests
            val value = id?.let { databaseRequests!!.child(it).setValue(inventory) }

            //setting edit text to blank again


            //displaying a success toast
            Toast.makeText(this, "Request added", Toast.LENGTH_LONG).show()


        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
        }
    }



    private fun initializeUI() {
        mItemEditText = findViewById(R.id.activityAddRequest_itemEditText)
        mQuantityDesiredEditText = findViewById(R.id.activityAddRequest_quantityDesiredEditText)
        mCurrentQuantityEditText = findViewById(R.id.activityAddRequest_currentQuantityEditText)
        mFinishDateEditText = findViewById(R.id.activityAddRequest_finishDateEditText)
        mAddRequestButton= findViewById(R.id.activityAddRequest_addRequestButton)


    }




}
