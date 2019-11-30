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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DonorEditActivity : AppCompatActivity() {
    private lateinit var mTypeSpinner: Spinner
    private lateinit var mNameEditText: EditText
    private lateinit var mPhoneEditText: EditText
    private lateinit var mSaveButton: Button
    private lateinit var mProgressBar: ProgressBar

    private lateinit var typeList : Array<String>

    private var userID = "-1"
    private var donorID = "-1"

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_edit)

        initializeViews()
        initializeValues()

        mSaveButton.setOnClickListener {
            saveExistingUser()
        }
    }

    private fun saveExistingUser() {
        mProgressBar.visibility = View.VISIBLE

        val type: String     = mTypeSpinner.selectedItem.toString().trim { it <= ' ' }
        val name: String     = mNameEditText.text.toString().trim { it <= ' ' }
        val phone: String    = mPhoneEditText.text.toString().trim { it <= ' ' }

        if (TextUtils.isEmpty(type)) {
            Toast.makeText(applicationContext, "Please enter a type!", Toast.LENGTH_LONG).show()
            mProgressBar.visibility = View.GONE
            return
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(applicationContext, "Please enter a name!", Toast.LENGTH_LONG).show()
            mProgressBar.visibility = View.GONE
            return
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(applicationContext, "Please enter a phone!", Toast.LENGTH_LONG).show()
            mProgressBar.visibility = View.GONE
            return
        }

        // Displays successful registration toast
        Toast.makeText(
            applicationContext,
            "Registration successful!",
            Toast.LENGTH_LONG
        ).show()
        mProgressBar.visibility = View.GONE

        // Creates provider entry
        val mDatabase          = FirebaseDatabase.getInstance()
        val mDatabaseReference = mDatabase.getReference("donors").child(userID)

        val donor = Donor(userID, donorID, type, name, phone)
        mDatabaseReference.child(donorID).setValue(donor)

        finish()
    }

    private fun initializeViews() {
        mTypeSpinner   = findViewById(R.id.activityDonorRegistration_typeSpinner)
        mNameEditText  = findViewById(R.id.activityDonorRegistration_nameEditText)
        mSaveButton    = findViewById(R.id.activityDonorEdit_saveButton)
        mProgressBar   = findViewById(R.id.activityDonorRegistration_progressBar)
        mPhoneEditText = findViewById(R.id.activityDonorRegistration_phoneEditText)

        typeList = resources.getStringArray(R.array.donor_type_array)

        ArrayAdapter.createFromResource(
            this,
            R.array.donor_type_array,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mTypeSpinner.adapter = adapter
        }
    }

    private fun initializeValues() {
        sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCE, Context.MODE_PRIVATE)

        userID  = sharedPreferences.getString(LoginActivity.USER_ID, "default_id").toString()
        donorID = sharedPreferences.getString(LoginActivity.USER_DONOR_ID, "default_donor_id").toString()

        val mDatabase = FirebaseDatabase.getInstance()
        val mDistributorDatabaseReference = mDatabase.getReference("donors").child(userID)

        mDistributorDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val donor = postSnapshot.getValue<Donor>(Donor::class.java)

                    if (donor != null) {
                        mNameEditText.setText(donor.donorName)
                        mPhoneEditText.setText(donor.donorPhone)

                        var index = 0
                        for (typeString : String in typeList) {
                            if (typeString == donor.donorType) {
                                break
                            }
                            index += 1
                        }
                        mTypeSpinner.setSelection(index)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
