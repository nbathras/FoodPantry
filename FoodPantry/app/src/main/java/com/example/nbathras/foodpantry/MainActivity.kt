package com.example.nbathras.foodpantry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var mDonorRegisterButton: Button?        = null
    private var mProviderRegistrationButton: Button? = null
    private var mLoginButton: Button?                = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()

        mDonorRegisterButton!!.setOnClickListener {
            val intent = Intent(this@MainActivity, DonorRegistrationActivity::class.java)
            startActivity(intent)
        }
        mProviderRegistrationButton!!.setOnClickListener {
            val intent = Intent(this@MainActivity, ProviderRegistrationActivity::class.java)
            startActivity(intent)
        }
        mLoginButton!!.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initializeViews() {
        mDonorRegisterButton        = findViewById(R.id.activityMain_registerDonorButton)
        mProviderRegistrationButton = findViewById(R.id.activityMain_registerProviderButton)
        mLoginButton                = findViewById(R.id.login)
    }
}

