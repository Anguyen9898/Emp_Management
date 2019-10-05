package com.example.employeemanagement.adapters.Firebase

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

abstract class GetFirebaseValues(var mContext: Context? = null) : ValueEventListener {

    abstract override fun onDataChange(data: DataSnapshot)

    override fun onCancelled(error: DatabaseError){
        ProgressDialog(mContext).dismiss()
        Log.w(TAG, "Failed to read value.", error.toException())
    }
}