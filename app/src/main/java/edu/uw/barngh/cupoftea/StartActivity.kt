package edu.uw.barngh.cupoftea

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Button
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class StartActivity : AppCompatActivity() {

    var MY_PERMISSIONS_REQUEST_LOC : Int = 0
    var mCurrentLocation : Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    var locationRequest : LocationRequest? = null
    val TAG = "startlocation"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOC
                )
            }
        } else {
            // Permission has already been granted
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
//                Log.v(TAG, "last Location")
//                Log.v(TAG, "${location!!.longitude}, ${location!!.latitude}")
                mCurrentLocation = location
//                mapFragment.getMapAsync(this)

            }


        findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

            sharedPref.edit().putFloat(getString(R.string.key_location_long), mCurrentLocation!!.longitude.toFloat()).apply()

            sharedPref.edit().putFloat(getString(R.string.key_location_lat), mCurrentLocation!!.latitude.toFloat()).apply()

                val intent = Intent(this, NameActivity::class.java)
                this.startActivity(intent)
        }
//            val intent = Intent(this, NameActivity::class.java)
//            val intent = Intent(this, PersonListActivity::class.java)
//            this.startActivity(intent)
        }

}
