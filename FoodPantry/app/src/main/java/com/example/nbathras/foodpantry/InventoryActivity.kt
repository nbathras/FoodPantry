package com.example.nbathras.foodpantry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.database.*


class InventoryActivity : AppCompatActivity() {

    private lateinit var mItemEditText: EditText
    private lateinit var mQuantityEditText: EditText
    private lateinit var mDropOffEditText: EditText
    private lateinit var mAddRequestButton : Button
    private lateinit var mBackButton : Button
    var databaseInventories: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_inventory);
        databaseInventories = FirebaseDatabase.getInstance().getReference("inventories")
        initializeUI()

        mAddRequestButton.setOnClickListener {
            addRequest()
        }

        mBackButton.setOnClickListener {
            finish()
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
        val quantity: String = mQuantityEditText.text.toString()
        val dropOff: String     = mDropOffEditText.text.toString()

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

        // we are gonna save inventory to databse


        addRequestObjectToDatabase(item,quantity,dropOff)
        //launch login activity
        val intent = Intent(this@InventoryActivity, DistributorSplashActivity::class.java)
        startActivity(intent)



    }

    private fun addRequestObjectToDatabase(item : String, quantity: String, dropOff: String) {
        //getting the values to save


        //checking if the value is provided
        if (!TextUtils.isEmpty(item)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            // val id = databaseInventories.push().getKey()
            val id = databaseInventories!!.push().key;
            //creating an Artist Object
            val inventory = Inventory(item, quantity, dropOff)

            //Saving the Artist
            val value = id?.let { databaseInventories!!.child(it).setValue(inventory) }

            //setting edittext to blank again


            //displaying a success toast
            Toast.makeText(this, "Inventory added", Toast.LENGTH_LONG).show()






        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
        }
    }



    private fun initializeUI() {
        mItemEditText = findViewById(R.id.activityAddInventory_itemEditText)
        mQuantityEditText = findViewById(R.id.activityAddInventory_quantityEditText)
        mDropOffEditText = findViewById(R.id.activityAddInventory_dropOffEditText)
        mAddRequestButton= findViewById(R.id.activityAddInventory_addRequestButton)
        mBackButton= findViewById(R.id.activityAddInventory_backButton)


    }




}
