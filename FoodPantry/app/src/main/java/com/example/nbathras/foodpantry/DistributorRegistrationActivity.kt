package com.example.nbathras.foodpantry

import android.app.ActionBar
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DistributorRegistrationActivity : AppCompatActivity() {

    private lateinit var mEmailEditText: EditText
    private lateinit var mPasswordEditText: EditText
    private lateinit var mNameEditText: EditText
    private lateinit var mAddressEditText: EditText
    private lateinit var mAboutEditText: EditText
    private lateinit var mRegistrationButton: Button
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mAdditionAddressLinearLayout : LinearLayout
    private lateinit var mAddAddressButton : Button

    private lateinit var mAuth: FirebaseAuth

    private var numAddressEditText = 1

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_distributor_registration)

        initializeViews()

        mRegistrationButton.setOnClickListener {
            registerNewUser()
        }

        mAddAddressButton.setOnClickListener {
            numAddressEditText += 1

            val edit_text = EditText(this)
            edit_text.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            edit_text.hint = getString(R.string.address_edit_text_hint) + " " + numAddressEditText.toString()
            mAdditionAddressLinearLayout.addView(edit_text)
        }
    }

    private fun registerNewUser() {
        mProgressBar.visibility = View.VISIBLE

        val email: String    = mEmailEditText.text.toString()
        val password: String = mPasswordEditText.text.toString()
        val name: String     = mNameEditText.text.toString().trim { it <= ' ' }
        val address: String  = mAddressEditText.text.toString().trim { it <= ' ' }
        val about: String    = mAboutEditText.text.toString().trim { it <= ' ' }

        var locationList: ArrayList<String> = ArrayList<String>()
        locationList.add(address)

        for (nextView: View in mAdditionAddressLinearLayout.children) {
            val mNextLocationEditText = nextView as EditText
            locationList.add(mNextLocationEditText.text.toString().trim { it <= ' ' })
        }

        // ToDo: Probably should check if the email entered was valid as well
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email!", Toast.LENGTH_LONG).show()
            return
        }
        // ToDo: Probably should do a check if it is the correct length and the correct number of different character types
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        }

        mAuth = FirebaseAuth.getInstance()
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

                    // Creates distributor entry
                    val userID             = mAuth.currentUser!!.uid
                    val mDatabase          = FirebaseDatabase.getInstance()
                    val mDatabaseReference = mDatabase.getReference("distributors").child(userID)

                    val id = (mDatabaseReference.push()).key.toString()
                    val distributor = Distributor(userID, id, name, about, locationList)
                    mDatabaseReference.child(id).setValue(distributor)

                    // Opens login activity
                    val intent = Intent(this@DistributorRegistrationActivity, LoginActivity::class.java)
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
        mEmailEditText               = findViewById(R.id.activityDistributorRegistration_emailEditText)
        mPasswordEditText            = findViewById(R.id.activityDistributorRegistration_passwordEditText)
        mNameEditText                = findViewById(R.id.activityDistributorRegistration_nameEditText)
        mAddressEditText             = findViewById(R.id.activityDistributorRegistration_addressEditText)
        mAboutEditText               = findViewById(R.id.activityDistributorRegistration_aboutEditText)
        mRegistrationButton          = findViewById(R.id.activityDistributorRegistration_registerButton)
        mProgressBar                 = findViewById(R.id.activityDistributorRegistration_progressBar)
        mAdditionAddressLinearLayout = findViewById(R.id.activityDistributorRegistration_additionAddressLinearLayout)
        mAddAddressButton            = findViewById(R.id.activityDistributorRegistration_addAddressbutton)
    }

    companion object {
        private const val TAG          = "FoodPantry"
        private const val ACTIVITY_TAG = "DistributorRegistrationActivity"
    }
}
