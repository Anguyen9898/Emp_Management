package com.example.employeemanagement.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.employeemanagement.R
import com.example.employeemanagement.adapters.Firebase.FireBaseAdapter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.example.employeemanagement.adapters.GetCurrentLocation
import com.example.employeemanagement.adapters.Firebase.GetFirebaseValues
import com.example.employeemanagement.supporters.MarkerAnimation
import com.example.employeemanagement.supporters.interfaces.Support
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot

@Suppress("DEPRECATION")
class ManagerHomeFrag(private var mMap: GoogleMap? = null): GetCurrentLocation(null,
    null, null, null, null,
    null)
    , OnMapReadyCallback{

    private val markerAnimate = MarkerAnimation()
    private var currentLocation: Marker? = null

    private val firebaseAdapter = object : FireBaseAdapter(){}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mContext = this.context
        firebaseAdapter.setContext(this.context!!)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_manager_home, container, false)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        setLocation()

        return view
    }

    override fun onStart() {
        super.onStart()
        if(isLocationEnabled){
            getLastLocation()
            startLocationUpdates()
        }else {
            checkLocation()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK && data != null ){
                //val d = data.data
                getLastLocation()
                startLocationUpdates()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getStaffLocation()
    }

    override fun setResultOnFragment(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        val hashMap = HashMap<String, Any?>()
        hashMap["Latitude"] = latitude
        hashMap["Longitude"] = longitude
        firebaseAdapter.managerRefWithUid().apply {
               addValueEventListener(object : GetFirebaseValues(context) {
                   override fun onDataChange(data: DataSnapshot) {
                       currentLocation = mMap!!.addMarker(MarkerOptions()
                               .position(latLng)
                               .title(data.child("Employee ID").value.toString())
                       )
                       mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 50f))
                       markerAnimate.animateMarkerToGB(currentLocation!!, latLng,
                           Support.Spherical()
                       )
                   }
               })

               child("Location").updateChildren(hashMap)
           }
    }

    private fun getStaffLocation(){
        firebaseAdapter.staffReference()
            .addValueEventListener(object : GetFirebaseValues(context){
                override fun onDataChange(data: DataSnapshot) =
                    data.children.forEach {
                        if (it.hasChild("Location")){
                            val latLng = LatLng((it.child("Location")
                                .child("Latitude").value) as Double
                                , (it.child("Location")
                                    .child("Longitude").value) as Double)

                            currentLocation = mMap!!.addMarker(getStaffMarkerOptions(latLng))

                            markerAnimate.animateMarkerToGB(currentLocation!!, latLng
                                , Support.Spherical())
                        }
                    }
            })
    }

    private fun getStaffMarkerOptions(position : LatLng) : MarkerOptions{
        return MarkerOptions().apply {
            icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker))
            position(position)
            flat(true)
        }
    }
}