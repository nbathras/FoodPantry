package com.example.nbathras.foodpantry

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DistributorEditActivity : AppCompatActivity() {

    private lateinit var mNameEditText: EditText
    private lateinit var mAddressEditText: EditText
    private lateinit var mAboutEditText: EditText
    private lateinit var mSaveButton: Button
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mAdditionAddressLinearLayout : LinearLayout
    private lateinit var mAddAddressButton : Button

    private var numAddressEditText = 1
    private var userID = "-1"
    private var distributorID = "-1"

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_edit)

        initializeViews()
        initializeValues()

        mSaveButton.setOnClickListener {
            saveExistingUser()
        }
    }

    private fun saveExistingUser() {
        mProgressBar.visibility = View.VISIBLE

        val name: String     = mNameEditText.text.toString().trim { it <= ' ' }
        val address: String  = mAddressEditText.text.toString().trim { it <= ' ' }
        val about: String    = mAboutEditText.text.toString().trim { it <= ' ' }

        var locationList: ArrayList<String> = ArrayList<String>()
        locationList.add(address)

        for (nextView: View in mAdditionAddressLinearLayout.children) {
            val mNextLocationEditText = nextView as EditText
            locationList.add(mNextLocationEditText.text.toString().trim { it <= ' ' })
        }

        Toast.makeText(
            applicationContext,
            "Registration successful!",
            Toast.LENGTH_LONG
        ).show()
        mProgressBar.visibility = View.GONE

        // Creates distributor entry
        val mDatabase          = FirebaseDatabase.getInstance()
        val mDatabaseReference = mDatabase.getReference("distributors").child(userID)

        val distributor = Distributor(userID, distributorID, name, about, locationList)
        mDatabaseReference.child(distributorID).setValue(distributor)

        finish()
    }

    private fun initializeViews() {
        mNameEditText                = findViewById(R.id.activityDistributorRegistration_nameEditText)
        mAddressEditText             = findViewById(R.id.activityDistributorRegistration_addressEditText)
        mAboutEditText               = findViewById(R.id.activityDistributorRegistration_aboutEditText)
        mSaveButton                  = findViewById(R.id.activityDistributorEdit_saveButton)
        mProgressBar                 = findViewById(R.id.activityDistributorRegistration_progressBar)
        mAdditionAddressLinearLayout = findViewById(R.id.activityDistributorRegistration_additionAddressLinearLayout)
        mAddAddressButton            = findViewById(R.id.activityDistributorRegistration_addAddressbutton)
    }

    private fun initializeValues() {
        sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCE, Context.MODE_PRIVATE)

        userID        = sharedPreferences.getString(LoginActivity.USER_ID, "default_id").toString()
        distributorID = sharedPreferences.getString(LoginActivity.USER_DISTRIBUTOR_ID, "default_distributor_id").toString()

        val mDatabase = FirebaseDatabase.getInstance()
        val mDistributorDatabaseReference = mDatabase.getReference("distributors").child(userID)

        mDistributorDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val distributor = postSnapshot.getValue<Distributor>(Distributor::class.java)

                    if (distributor != null) {
                        mNameEditText.setText(distributor.distributorName)
                        mAboutEditText.setText(distributor.distributorAbout)
                        mAddressEditText.setText(distributor.distributorLocation.get(0))

                        var first = true
                        for (nextAddress: String in distributor.distributorLocation) {
                            if (first) {
                                first = false
                                continue
                            }

                            val edit_text = EditText(this@DistributorEditActivity)
                            edit_text.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT)
                            edit_text.hint = getString(R.string.address_edit_text_hint) + " " + numAddressEditText.toString()
                            edit_text.setText(nextAddress)
                            mAdditionAddressLinearLayout.addView(edit_text)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
