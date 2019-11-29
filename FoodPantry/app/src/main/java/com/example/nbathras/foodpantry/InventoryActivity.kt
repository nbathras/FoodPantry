package com.example.nbathras.foodpantry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class InventoryActivity : AppCompatActivity() {

    private lateinit var mItemEditText: EditText
    private lateinit var mQuantityEditText: EditText
    private lateinit var mDropOffEditText: EditText
    private lateinit var mAddRequestButton : Button
    private lateinit var mBackButton : Button

    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_inventory)

        mAuth = FirebaseAuth.getInstance()


        initializeUI()

        mAddRequestButton.setOnClickListener {
            addRequest()
        }

        mBackButton.setOnClickListener {
            finish()
        }
    }

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


        //launch login activity
        val intent = Intent(this@InventoryActivity, DistributorSplashActivity::class.java)
        startActivity(intent)



    }

    private fun initializeUI() {
      mItemEditText = findViewById(R.id.activityAddInventory_itemEditText)
        mQuantityEditText = findViewById(R.id.activityAddInventory_quantityEditText)
        mDropOffEditText = findViewById(R.id.activityAddInventory_dropOffEditText)
        mAddRequestButton= findViewById(R.id.activityAddInventory_addRequestButton)
        mBackButton= findViewById(R.id.activityAddInventory_backButton)


    }




}
