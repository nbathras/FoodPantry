package com.example.nbathras.foodpantry

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import android.widget.ListView
import android.widget.ArrayAdapter
import android.view.View

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
        databaseDistributors = FirebaseDatabase.getInstance().getReference("distributors").child(userUID)
        //distributors arraylist to be populated with values from databaseDistributors
        distributors = ArrayList()
        //List view defined in layout file
        listViewDistributor = findViewById<View>(R.id.donor_splash_list) as ListView
    }

    override fun onStart() {
        super.onStart()
        databaseDistributors.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous distributors list
                distributors.clear()

                //populating the distributors array from the database
                for (postSnapshot in dataSnapshot.children) {
                    val distributor = postSnapshot.getValue<Distributor>(Distributor::class.java)
                    //add distributor to the list
                    distributors.add(distributor!!)
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

    companion object {
        const val MY_PREFERENCE   = "myPreference"
        const val USER_ID         = "userID"
    }

}