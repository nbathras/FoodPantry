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

class LoginActivity : AppCompatActivity() {

    private var mEmailEditText : EditText? = null
    private var mPasswordEditText : EditText? = null
    private var mLoginButton : Button? = null
    private var mProgressBar : ProgressBar? = null

    private var mAuth : FirebaseAuth? = null

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

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_LONG).show()
                    mProgressBar!!.visibility = View.GONE

                    val id = mAuth?.currentUser?.uid
                    /*
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    intent.putExtra(UserID, id)
                    startActivity(intent)
                    */
                } else {
                    // ToDo: Probably should have more explicit failure messages
                    Toast.makeText(applicationContext, "Registration failed!  Please try again later", Toast.LENGTH_LONG).show()
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
        val UserID = "com.example.tesla.myhomelibrary.UID"
    }
}
