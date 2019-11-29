package com.example.nbathras.foodpantry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.content.SharedPreferences
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ListView
import android.widget.ArrayAdapter
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nbathras.foodpantry.DistributorPageRequestActivity.Companion.DISTRIBUTOR_ABOUT
import com.example.nbathras.foodpantry.DistributorPageRequestActivity.Companion.DISTRIBUTOR_ID
import com.example.nbathras.foodpantry.DistributorPageRequestActivity.Companion.DISTRIBUTOR_NAME
import com.example.nbathras.foodpantry.LoginActivity.Companion.MY_PREFERENCE

import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class DonorSplashActivity : AppCompatActivity() {

    private lateinit var databaseDistributors: DatabaseReference
    private lateinit var userUID: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var distributors: MutableList<Distributor>
    private lateinit var listViewDistributor: ListView
    private lateinit var listViewAdapter: ArrayAdapter<Distributor>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_splash)

        sharedPreferences = getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE)
        //Obtaining specific userID through sharedPreferences
        userUID = sharedPreferences.getString(USER_ID, "Null").toString()
        //Accessing distributors list from firebase database
        databaseDistributors = FirebaseDatabase.getInstance().getReference("distributors")
        //distributors arraylist to be populated with values from databaseDistributors
        distributors = ArrayList()
        //List view defined in layout file
        listViewDistributor = findViewById<View>(R.id.donor_splash_list) as ListView

        //When the user clicks on a specific distributor list item, show their biography/request page
        listViewDistributor.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val distributor = distributors[i]
                val intent = Intent(applicationContext, DistributorPageRequestActivity::class.java)


                //This is a test
                intent.putExtra(DISTRIBUTOR_NAME, distributor.distributorName)
                intent.putExtra(DISTRIBUTOR_ABOUT, distributor.distributorAbout)
                intent.putExtra(USER_ID, userUID)
                intent.putExtra(DISTRIBUTOR_ID,distributor.distributorId)

                startActivity(intent)
            }
    }

    override fun onStart() {
        super.onStart()
        databaseDistributors.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous distributors list
                distributors.clear()

                //populating the distributors array from the database
                for (postSnapshot in dataSnapshot.children) {
                    for (postSnapshot2 in postSnapshot.children) {
                        val distributor = postSnapshot2.getValue<Distributor>(Distributor::class.java)
                        //add distributor to the list
                        distributors.add(distributor!!)
                    }
                }

                //Initializing listViewAdapter to customized DistributorList adapter
                listViewAdapter = DistributorList(this@DonorSplashActivity, distributors)
                listViewDistributor.adapter = listViewAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                //Empty
            }
        })
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
            startActivity(intent)

            return true
        }
        if (item.itemId == R.id.loggedInMenu_editProfileItem) {
            val intent = Intent(this, DonorEditActivity::class.java)
            startActivity(intent)

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val MY_PREFERENCE   = "myPreference"
        const val USER_ID         = "userID"

        const val DISTRIBUTOR_NAME  = "distributorName"
        const val DISTRIBUTOR_ABOUT = "distributorAbout"
        const val DISTRIBUTOR_ADDRESS = "distributorAddress"
        const val DISTRIBUTOR_ID = "distributorID"

        private val TAG = "DonorSplashActivity"
    }

}