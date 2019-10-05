

package com.example.employeemanagement.adapters

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.employeemanagement.supporters.interfaces.Support
import com.google.android.gms.location.*

abstract class GetCurrentLocation(
    protected var mContext: Context?,
    private var mLastLocation: Location?,
    private var mLocationRequest: LocationRequest?,
    private var mLocationManager: LocationManager?,
    private var mLocationCallback: LocationCallback?,
    private var mFusedLocationProviderClient: FusedLocationProviderClient?): Fragment(),
    Support {

    constructor() : this(null,
        null, null,
        null, null, null)

    private var UPDATE_INTERVAL: Long = 2 * 1000 //10 secs
    private var FASTEST_INTERVAL: Long = 2000 //2 secs

    protected val isLocationEnabled: Boolean
        get() {
            mLocationManager = mContext!!.getSystemService(Context.LOCATION_SERVICE)
                    as LocationManager
            return mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

    protected fun setLocation() {
        try {
            mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(mContext!!)
            mFusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        //setResultOnFragment(location.latitude, location.longitude)
                        Toast.makeText(mContext, "Set value successfully", Toast.LENGTH_LONG)
                            .show()
                    }
            }
            mLocationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = UPDATE_INTERVAL
                fastestInterval = FASTEST_INTERVAL
            }

            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult!!.locations) {
                        setResultOnFragment(location!!.latitude, location.longitude)
                    }
                }
            }
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun getLastLocation(){
        mFusedLocationProviderClient!!.lastLocation
            .addOnCompleteListener{task ->
                if(task.isSuccessful and(task.result != null)){
                    mLastLocation = task.result
                    setResultOnFragment(mLastLocation!!.latitude, mLastLocation!!.longitude)
                }else
                    Toast.makeText(mContext, "Failed to get location", Toast.LENGTH_LONG)
                        .show()
            }
    }

    abstract fun setResultOnFragment(latitude: Double, longitude: Double)

    override fun onPause() {
        stopLocationUpdates()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults.size > 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {

                    Toast.makeText(mContext, "Permission granted!", Toast.LENGTH_LONG)
                        .show()
                } else
                    Toast.makeText(mContext, "Permission denied!", Toast.LENGTH_LONG)
                        .show()
            }
        }
    }

    protected fun startLocationUpdates() {
        //Request location updates
        if (checkSDKVer()) {
            if(!checkIfPermission(mContext!!)){
                mFusedLocationProviderClient!!
                    .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
            }
            else {
                askForPermission(mContext!!)
            }
        }
    }

    private fun stopLocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }

    protected fun checkLocation(): Boolean {
        //if (!isLocationEnabled) {
            setAlertDialog(
                mContext, "GPS Setting"
                , "GPS is not enabled. Please go to setting and turn your gps on!"
            )
                //On pressing Setting button
                .setPositiveButton("Setting") { _, _ ->
                    //DialogInterface.OnClickListener
                    startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                        101)
                }
                .show()
        //}
        return isLocationEnabled
    }
}



  /*  /**
     * Google play services check on device Method
     * */

    protected fun checkGPlayServices() : Boolean{
        val resultCode = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(this.mContext)
        return if(resultCode != ConnectionResult.SUCCESS) {
            if (GoogleApiAvailability.getInstance().isUserResolvableError(resultCode))
                GoogleApiAvailability.getInstance().getErrorDialog(this.mContext as Activity, resultCode, 1000)
                    .show()
            else
                Toast.makeText(this.mContext, "This device is not available", Toast.LENGTH_LONG)
                    .show()
            false
        } else true
    }


    }*/