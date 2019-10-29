package com.example.nbathras.foodpantry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class DonorRegistrationActivity : AppCompatActivity() {

    private var mEmailEditText: EditText?    = null
    private var mPasswordEditText: EditText? = null
    private var mRegistrationButton: Button? = null
    private var mProgressBar: ProgressBar?   = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_registration)

        mAuth = FirebaseAuth.getInstance()

        initializeViews()

        mRegistrationButton!!.setOnClickListener {
            registerNewUser()
        }
    }

    private fun registerNewUser() {
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

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG).show()
                    mProgressBar!!.visibility = View.GONE

                    val intent = Intent(this@DonorRegistrationActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // ToDo: Probably should have more explicit failure messages
                    Toast.makeText(applicationContext, "Registration failed!  Please try again later", Toast.LENGTH_LONG).show()
                    mProgressBar!!.visibility = View.GONE
                }
            }
    }

    private fun initializeViews() {
        mEmailEditText      = findViewById(R.id.activityDonorRegistration_emailEditText)
        mPasswordEditText   = findViewById(R.id.activityDonorRegistration_passwordEditText)
        mRegistrationButton = findViewById(R.id.activityDonorRegistration_registerButton)
        mProgressBar        = findViewById(R.id.activityDonorRegistration_progressBar)
    }
}
