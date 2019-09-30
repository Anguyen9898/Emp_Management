package com.example.employeemanagement.supporters

import android.os.SystemClock
import android.view.animation.AccelerateDecelerateInterpolator
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import android.os.Handler
import com.example.employeemanagement.supporters.interfaces.LatLngInterpolator

class MarkerAnimation {

    fun animateMarkerToGB(marker: Marker, finalPos: LatLng
                          , latlngInterpolator: LatLngInterpolator
    ){
        val starPos = marker.position
        val start = SystemClock.uptimeMillis()
        val interpolator = AccelerateDecelerateInterpolator()
        val durationInMs = 2000f
        val handler = Handler()

        handler.post(object : Runnable {
            var elapsed = 0L
            var t = 0f
            var v = 0f

            override fun run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start
                t = elapsed/ durationInMs
                v = interpolator.getInterpolation(t)
                marker.position = latlngInterpolator.interpolate(v, starPos, finalPos)

                // Repeat till progress is complete
                if(t < 1){
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                }
            }
        })

    }
}