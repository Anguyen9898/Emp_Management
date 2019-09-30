package com.example.employeemanagement.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.employeemanagement.R
import com.example.employeemanagement.models.adapters.FireBaseAdapter
import com.example.employeemanagement.models.adapters.GetCurrentLocation
import com.example.employeemanagement.models.adapters.StaffAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class StaffHomeFrag : GetCurrentLocation(null,
    null, null,
    null, null, null) {
    private var reportLocation : Button?= null
    private var progressDialog: ProgressDialog?= null

    private val staff = StaffAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mContext = this.context
        setLocation()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_staff_home, container, false)
        reportLocation = view.findViewById(R.id.shareLocation)
        reportLocation!!.setOnClickListener(clickListener)
        return view
    }

    private val clickListener = View.OnClickListener {
        if(!isLocationEnabled) {
            startLocationUpdates()
            checkLocation()
        }
        else{
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("Please wait...")
            progressDialog!!.show()
            getLastLocation()
            startLocationUpdates()
        }
    }

    override fun setResultOnFragment(latitude: Double, longitude: Double) {
        val hashMap = HashMap<String, Any?>()
        hashMap["Latitude"] = latitude
        hashMap["Longitude"] = longitude
        staff.updateStaffValue(hashMap)!!.addOnCompleteListener { task->
                if(task.isSuccessful) {
                    progressDialog!!.dismiss()
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                }
                else
                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
            }
    }
}
