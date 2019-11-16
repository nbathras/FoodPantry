package com.example.nbathras.foodpantry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class DistributorSplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_splash)

        // look at the login activity
        // all of the basic information like whether the user is a donor or distributor as well as
        // the email, name, address, about are all stored in shared preferences so we do not have to
        // continue doing gets every time we want to display the user profile
        // System will probably change in soon when I come up with a better way to store everything
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logged_in_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.loggedInMenu_logOutItem) {
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent);

            return true
        }
        if (item.itemId == R.id.loggedInMenu_editProfileItem) {
            Toast.makeText(this, "This should edit profile", Toast.LENGTH_SHORT).show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
