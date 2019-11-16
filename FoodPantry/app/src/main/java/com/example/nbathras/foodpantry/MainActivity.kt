package com.example.nbathras.foodpantry

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var mDonorRegisterButton: Button?           = null
    private var mDistributorRegistrationButton: Button? = null
    private var mLoginButton: Button?                   = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ToDo: Do a check of the sharedUserPreference and open the desired page if the preference is filled

        setContentView(R.layout.activity_main)

        initializeViews()

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



    private fun initializeViews() {
        mDonorRegisterButton        = findViewById(R.id.activityMain_registerDonorButton)
        mDistributorRegistrationButton = findViewById(R.id.activityMain_registerDistributorButton)
        mLoginButton                = findViewById(R.id.login)
    }
}

