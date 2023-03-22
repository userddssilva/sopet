package br.edu.uea.spd.sopet.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.adapter.UserChatAdapter
import br.edu.uea.spd.sopet.data.model.User
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatListActivity : AppCompatActivity() {
    companion object {
        private val TAG: String? = ChatListActivity::class.java.canonicalName
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var recycleView: RecyclerView
    private lateinit var adapterUser: UserChatAdapter
    private lateinit var usersList: ArrayList<User>

    // location request
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat_list)
//        supportActionBar?.hide()
        title = "Emoji Recommender"

        recycleView = findViewById(R.id.rc_users_list)
        recycleView.setHasFixedSize(true)
        recycleView.layoutManager = LinearLayoutManager(applicationContext)

        usersList = arrayListOf()

        getAllUsers()

    }

    override fun onResume() {
        super.onResume()

        // Create an instance of the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check if the app has permission to access the device's location
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, proceed with using the location
            // ...
            getUserLocation()
        } else {
            // Permission not yet granted, request it from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with using the location
                // ...
                getUserLocation()

            } else {
                // Permission denied, handle the error
                // ...
                requestLocationPermission()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Use the location
                        latitude = location.latitude
                        longitude = location.longitude
                        // ...
                        Log.d(TAG, "Location - LAT: $latitude LONG: $longitude")
                    }
                }
        } else {
            // Permission denied, handle the error
            // ...
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.edit_profile -> {
            startActivity(Intent(this, ProfileActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun getAllUsers() {
        val fUser = FirebaseAuth.getInstance().currentUser
        val firebaseReference = FirebaseDatabase.getInstance().getReference("Users")

        firebaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                for (ds in snapshot.children) {
                    val mUser = ds.getValue(User::class.java)
                    if (!mUser?.uid.equals(fUser?.uid)) {
                        mUser?.let { usersList.add(it) }
                    }
                    adapterUser = UserChatAdapter(usersList)
                    recycleView.adapter = adapterUser
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}