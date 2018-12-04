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
import com.google.android.gms.location.*

class StartActivity : AppCompatActivity() {

    var MY_PERMISSIONS_REQUEST_LOC : Int = 0
    var mCurrentLocation : Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    var locationRequest : LocationRequest? = null
    val TAG = "startlocation"
    private val LOCATION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


//            val intent = Intent(this, NameActivity::class.java)
//            val intent = Intent(this, PersonListActivity::class.java)
//            this.startActivity(intent)


                findViewById<Button>(R.id.bt_get_started).setOnClickListener { v ->

                    startLocationRequest()
                    if(mCurrentLocation != null) {
                        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                        Log.v(TAG, "clicked update location")
                        Log.v(TAG, mCurrentLocation!!.longitude.toString() + mCurrentLocation!!.latitude.toString())
                        Log.v(TAG, mCurrentLocation.toString())
                        sharedPref.edit()
                            .putFloat(getString(R.string.key_location_long), mCurrentLocation!!.longitude.toFloat())
                            .apply()
                        sharedPref.edit()
                            .putFloat(getString(R.string.key_location_lat), mCurrentLocation!!.latitude.toFloat())
                            .apply()
                        val intent = Intent(this, NameActivity::class.java)
                        this.startActivity(intent)
                    } else {
                        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                        Log.v(TAG, mCurrentLocation.toString())
                        sharedPref.edit().putFloat(getString(R.string.key_location_long), 0.toFloat()).apply()

                        sharedPref.edit().putFloat(getString(R.string.key_location_lat), 0.toFloat()).apply()

                        val intent = Intent(this, NameActivity::class.java)
                        this.startActivity(intent)
                    }
        }
        }

    override fun onStart() {
        super.onStart()
        startLocationRequest()
    }

    override fun onStop() {
        super.onStop()
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==  PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }

    }

    fun startLocationRequest() {
        Log.v(TAG, "get location")
        var permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            val locationRequest = LocationRequest().apply {
                this.interval = 1
                this.fastestInterval = 1
                this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationRequest: LocationResult?) {

                    locationRequest?.let {
                        Log.v(TAG, "${locationRequest.locations}")
                        mCurrentLocation = locationRequest.locations[0]
                        Log.v(TAG, mCurrentLocation!!.latitude.toString())
                    }

                }
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)

            Log.v(TAG, "permission denied")
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationRequest()
                }
            } else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }


}
