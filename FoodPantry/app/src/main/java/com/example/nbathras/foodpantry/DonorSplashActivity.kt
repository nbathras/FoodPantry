package com.example.nbathras.foodpantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DonorSplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_splash)

        // look at the login activity
        // all of the basic information like whether the user is a donor or distributor as well as
        // the email, name, address, about are all stored in shared preferences so we do not have to
        // continue doing gets every time we want to display the user profile
        // System will probably change in soon when I come up with a better way to store everything
    }
}
