package com.example.nbathras.foodpantry

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private var mEmailEditText : EditText? = null
    private var mPasswordEditText : EditText? = null
    private var mLoginButton : Button? = null
    private var mProgressBar : ProgressBar? = null

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()

        mLoginButton!!.setOnClickListener {
            loginUserAccount()
        }
    }

    private fun loginUserAccount() {
        mProgressBar!!.visibility = View.VISIBLE

        val email: String    = mEmailEditText!!.text.toString()
        val password: String = mPasswordEditText!!.text.toString()

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

        val mAuth = FirebaseAuth.getInstance()
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Login successful!",
                        Toast.LENGTH_LONG
                    ).show()
                    mProgressBar!!.visibility = View.GONE

                    val userID = mAuth.currentUser!!.uid
                    val userEMAIL = mAuth.currentUser!!.email

                    val mDatabase                     = FirebaseDatabase.getInstance()
                    val mDonorDatabaseReference       = mDatabase.getReference("donors").child(userID)
                    val mDistributorDatabaseReference = mDatabase.getReference("distributors").child(userID)

                    mDonorDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.childrenCount > 0 ) {
                                Log.i(TAG, ACTIVITY_TAG.plus(": signInWithEmailAndPassword | donors"))

                                for (postSnapshot in dataSnapshot.children) {
                                    sharedPreferences = getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE)
                                    val donor = postSnapshot.getValue<Donor>(Donor::class.java)

                                    val editor = sharedPreferences.edit()
                                    editor.putString(USER_LOGIN_TYPE, "donors")
                                    editor.putString(USER_ID, userID)
                                    editor.putString(USER_EMAIL, userEMAIL)
                                    editor.putString(USER_NAME, donor?.donorName)
                                    editor.putString(USER_DONOR_TYPE, donor?.donorType)
                                    editor.apply()
                                    editor.commit()
                                }

                                val intent = Intent(this@LoginActivity, DonorSplashActivity::class.java)
                                startActivity(intent)

                                finish()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })

                    mDistributorDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.childrenCount > 0 ) {
                                Log.i(TAG, ACTIVITY_TAG.plus(": signInWithEmailAndPassword | distributors"))

                                for (postSnapshot in dataSnapshot.children) {
                                    sharedPreferences = getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE)
                                    val distributor = postSnapshot.getValue<Distributor>(Distributor::class.java)

                                    val editor = sharedPreferences.edit()
                                    editor.putString(USER_LOGIN_TYPE, "distributor")
                                    editor.putString(USER_ID, userID)
                                    editor.putString(USER_EMAIL, userEMAIL)
                                    editor.putString(USER_NAME, distributor?.distributorName)
                                    editor.putString(USER_DISTRIBUTOR_ABOUT, distributor?.distributorAbout)
                                    editor.putString(USER_DISTRIBUTOR_ADDRESS, distributor?.distributorAddress)
                                    editor.apply()
                                    editor.commit()
                                }

                                val intent = Intent(this@LoginActivity, DistributorSplashActivity::class.java)
                                startActivity(intent)

                                finish()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })

                } else {
                    // ToDo: Probably should have more explicit failure messages
                    Toast.makeText(
                        applicationContext,
                        "Registration failed!  Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                    mProgressBar!!.visibility = View.GONE
                }
            }
    }

    private fun initializeViews() {
        mEmailEditText = findViewById(R.id.activityLogin_emailEditText)
        mPasswordEditText = findViewById(R.id.activityLogin_passwordEditText)
        mLoginButton = findViewById(R.id.activityLogin_loginButton)
        mProgressBar = findViewById(R.id.activityLogin_progressBar)
    }

    companion object {
        private const val TAG          = "FoodPantry"
        private const val ACTIVITY_TAG = "LoginActivity"

        const val MY_PREFERENCE   = "myPreference"
        const val USER_LOGIN_TYPE = "userLoginType"
        const val USER_ID         = "userID"
        const val USER_EMAIL      = "userEmail"
        const val USER_NAME       = "userName"

        const val USER_DONOR_TYPE = "userDonorType"

        const val USER_DISTRIBUTOR_ADDRESS = "userDistributorAddress"
        const val USER_DISTRIBUTOR_ABOUT   = "userDistributorAbout"
    }
}
