package com.example.nbathras.foodpantry

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences : SharedPreferences
    private var mDonorRegisterButton: Button? = null
    private var mDistributorRegistrationButton: Button? = null
    private var mLoginButton: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ToDo: Do a check of the sharedUserPreference and open the desired page if the preference is filled

        setContentView(R.layout.activity_main)

        initializeViews()

        sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCE, Context.MODE_PRIVATE)



        mDonorRegisterButton!!.setOnClickListener {
            val intent = Intent(this@MainActivity, DonorRegistrationActivity::class.java)
            startActivity(intent)
        }
        mDistributorRegistrationButton!!.setOnClickListener {
            val intent = Intent(this@MainActivity, DistributorRegistrationActivity::class.java)
            startActivity(intent)
        }
        mLoginButton!!.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        var email = sharedPreferences.getString(LoginActivity.USER_Email, null)
        var password = sharedPreferences.getString(LoginActivity.USER_PASSWORD, null)


        // AutoSign In for Development purpose and comment them out after testing
//        if(email != null && password != null) {
//
//            loginUserAccount(email,password)
//        }
    }


    private fun loginUserAccount(email:String, password:String) {

        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userID = mAuth.currentUser!!.uid
                    val userEMAIL = mAuth.currentUser!!.email

                    val mDatabase                     = FirebaseDatabase.getInstance()
                    val mDonorDatabaseReference       = mDatabase.getReference("donors").child(userID)
                    val mDistributorDatabaseReference = mDatabase.getReference("distributors").child(userID)

                    mDonorDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.childrenCount > 0 ) {

                                for (postSnapshot in dataSnapshot.children) {
                                    sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCE, Context.MODE_PRIVATE)
                                    val donor = postSnapshot.getValue<Donor>(Donor::class.java)

                                    val editor = sharedPreferences.edit()
                                    editor.putString(LoginActivity.USER_LOGIN_TYPE, "donors")
                                    editor.putString(LoginActivity.USER_ID, userID)
                                    editor.putString(LoginActivity.USER_PASSWORD, password)
                                    editor.putString(LoginActivity.USER_DONOR_ID, donor?.donorId)
                                    editor.apply()
                                    editor.commit()
                                }

                                val intent = Intent(this@MainActivity, DonorSplashActivity::class.java)
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })

                    mDistributorDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.childrenCount > 0 ) {


                                for (postSnapshot in dataSnapshot.children) {
                                    sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCE, Context.MODE_PRIVATE)
                                    val distributor = postSnapshot.getValue<Distributor>(Distributor::class.java)

                                    val editor = sharedPreferences.edit()
                                    editor.putString(LoginActivity.USER_LOGIN_TYPE, "distributor")
                                    editor.putString(LoginActivity.USER_ID, userID)
                                    editor.putString(LoginActivity.USER_PASSWORD, password)
                                    editor.putString(LoginActivity.USER_DISTRIBUTOR_ID, distributor?.distributorId)
                                    editor.apply()
                                    editor.commit()
                                }

                                val intent = Intent(this@MainActivity, DistributorSplashActivity::class.java)
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })


                } else {

                }
            }
    }


    private fun initializeViews() {
        mDonorRegisterButton        = findViewById(R.id.activityMain_registerDonorButton)
        mDistributorRegistrationButton = findViewById(R.id.activityMain_registerDistributorButton)
        mLoginButton                = findViewById(R.id.login)
    }
}

