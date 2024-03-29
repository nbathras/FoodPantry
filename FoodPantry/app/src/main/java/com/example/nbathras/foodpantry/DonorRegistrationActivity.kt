package com.example.nbathras.foodpantry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DonorRegistrationActivity : AppCompatActivity() {

    private lateinit var mEmailEditText: EditText
    private lateinit var mPasswordEditText: EditText
    private lateinit var mTypeSpinner: Spinner
    private lateinit var mNameEditText: EditText
    private lateinit var mPhoneEditText: EditText
    private lateinit var mRegistrationButton: Button
    private lateinit var mProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_registration)

        initializeViews()

        mRegistrationButton.setOnClickListener {
            registerNewUser()
        }
    }

    private fun registerNewUser() {
        mProgressBar.visibility = View.VISIBLE

        val email: String    = mEmailEditText.text.toString()
        val password: String = mPasswordEditText.text.toString()
        val type: String     = mTypeSpinner.selectedItem.toString().trim { it <= ' ' }
        val name: String     = mNameEditText.text.toString().trim { it <= ' ' }
        val phone: String    = mPhoneEditText.text.toString().trim { it <= ' ' }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email!", Toast.LENGTH_LONG).show()
            mProgressBar.visibility = View.GONE
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            mProgressBar.visibility = View.GONE
            return
        }
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

        val mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, ACTIVITY_TAG.plus(": createUserWithEmailAndPassword"))

                    // Displays successful registration toast
                    Toast.makeText(
                        applicationContext,
                        "Registration successful!",
                        Toast.LENGTH_LONG
                    ).show()
                    mProgressBar.visibility = View.GONE

                    // Creates provider entry
                    val userID             = mAuth.currentUser!!.uid
                    val mDatabase          = FirebaseDatabase.getInstance()
                    val mDatabaseReference = mDatabase.getReference("donors").child(userID)

                    val id = (mDatabaseReference.push()).key.toString()
                    val donor = Donor(userID, id, type, name, phone)
                    mDatabaseReference.child(id).setValue(donor)

                    // Opens login activity
                    val intent = Intent(this@DonorRegistrationActivity, LoginActivity::class.java)
                    startActivity(intent)
                    // Closes activity
                    finish()
                } else {
                    // ToDo: Probably should have more explicit failure messages
                    Toast.makeText(
                        applicationContext,
                        "Registration failed!  Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                    mProgressBar.visibility = View.GONE
                }
            }
    }

    private fun initializeViews() {
        mEmailEditText      = findViewById(R.id.activityDonorRegistration_emailEditText)
        mPasswordEditText   = findViewById(R.id.activityDonorRegistration_passwordEditText)
        mTypeSpinner        = findViewById(R.id.activityDonorRegistration_typeSpinner)
        mNameEditText       = findViewById(R.id.activityDonorRegistration_nameEditText)
        mRegistrationButton = findViewById(R.id.activityDonorRegistration_registerButton)
        mProgressBar        = findViewById(R.id.activityDonorRegistration_progressBar)
        mPhoneEditText      = findViewById(R.id.activityDonorRegistration_phoneEditText)

        ArrayAdapter.createFromResource(
            this,
            R.array.donor_type_array,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mTypeSpinner.adapter = adapter
        }
    }

    companion object {
        private const val TAG          = "FoodPantry"
        private const val ACTIVITY_TAG = "DonorRegistrationActivity"
    }
}
