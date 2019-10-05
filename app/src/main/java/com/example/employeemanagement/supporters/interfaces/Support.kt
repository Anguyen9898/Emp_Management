package com.example.employeemanagement.supporters.interfaces

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.employeemanagement.R
import com.google.android.gms.maps.model.LatLng
import kotlin.math.pow

interface Support {

    /**
     * Check methods
     */
    fun checkSDKVer(): Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun checkIfPermission(mContext: Context): Boolean {
        return (ContextCompat.checkSelfPermission(mContext,
            Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
    }

    fun askForPermission(mContext: Context){
        //Permissions
        val arr = Array(2) {
            Manifest.permission.ACCESS_FINE_LOCATION
            Manifest.permission.ACCESS_COARSE_LOCATION
        }
        //Show an "ask for permission" dialog
        ActivityCompat.requestPermissions(mContext as Activity, arr, 100)
    }

    /**
     * Missing Required method
     */
    fun setRequiredColor(fieldMissing: Array<TextView>){
        for (i in fieldMissing){
            if(i.text.toString().isEmpty()) {
                i.setBackgroundResource(R.drawable.edittext_background_missingrequire)
                i.doOnTextChanged { _, _, _, _ ->
                    i.setBackgroundResource(R.drawable.edittext_background)
                }
            }
        }
    }

    /**
     * AlertDialog
     */
    fun setAlertDialog(mContext: Context?, title: String, Message: String): AlertDialog.Builder {
        return (AlertDialog.Builder(mContext).apply {
            //Setting Dialog Title
            setTitle(title)
            //Setting Dialog Message
            setMessage(Message)
            //On pressing Setting button
        })
    }

    /**
     * LatLngInterpolator
     */
    class Spherical() : LatLngInterpolator {

        override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
            // http://en.wikipedia.org/wiki/Slerp
            val fromLat = Math.toRadians(a.latitude)
            val fromLng = Math.toRadians(a.longitude)
            val toLat = Math.toRadians(b.latitude)
            val toLng = Math.toRadians(b.longitude)
            val cosFromLat = kotlin.math.cos(fromLat)
            val cosToLat = kotlin.math.cos(toLat)

            // Computes Spherical interpolation coefficients.
            val angle = computeAngleBetween(fromLat, fromLng, toLat, toLng)
            val sinAngle = kotlin.math.sin(angle)
            if (sinAngle < 1E-6) {
                return a
            }
            val temp1 = kotlin.math.sin((1 - fraction) * angle) / sinAngle
            val temp2 = kotlin.math.sin(fraction * angle) / sinAngle

            // Converts from polar to vector and interpolate.
            val x = temp1 * cosFromLat * kotlin.math.cos(fromLng) + temp2 * cosToLat * kotlin.math.cos(
                toLng
            )
            val y = temp1 * cosFromLat * kotlin.math.sin(fromLng) + temp2 * cosToLat * kotlin.math.sin(
                toLng
            )
            val z = temp1 * kotlin.math.sin(fromLat) + temp2 * kotlin.math.sin(toLat)

            // Converts interpolated vector back to polar.
            val lat = kotlin.math.atan2(z, kotlin.math.sqrt(x * x + y * y))
            val lng = kotlin.math.atan2(y, x)
            return LatLng(Math.toDegrees(lat), Math.toDegrees(lng))
        }

        private fun computeAngleBetween(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double): Double {
            val dLat = fromLat - toLat
            val dLng = fromLng - toLng
            return 2 * kotlin.math.asin(
                kotlin.math.sqrt(
                    kotlin.math.sin(dLat / 2).pow(2.0) + kotlin.math.cos(
                        fromLat
                    ) * kotlin.math.cos(toLat) * kotlin.math.sin(dLng / 2).pow(2.0)
                )
            )
        }
    }
}