package com.example.employeemanagement.supporters.interfaces

import com.google.android.gms.maps.model.LatLng
import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.pow


interface LatLngInterpolator {
    fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng
}

