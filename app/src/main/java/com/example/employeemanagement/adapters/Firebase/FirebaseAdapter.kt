package com.example.employeemanagement.adapters.Firebase

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.employeemanagement.supporters.interfaces.Support
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

abstract class FireBaseAdapter: AppCompatActivity(), Support {

    var mContext : Context? = null
    var firebaseAuth = FirebaseAuth.getInstance()
    var reference = FirebaseDatabase.getInstance().reference

    fun setContext(context: Context){
        mContext = context
    }

    fun getUid() : String{
        return firebaseAuth.currentUser!!.uid
    }

    fun employeesReference() : DatabaseReference{
        return reference.child("Employees")
    }

    fun employeesRefWithUid() : DatabaseReference{
        return reference.child("Employees").child(getUid())
    }

    fun managerRefWithUid() : DatabaseReference{
        return reference.child("Positions")
            .child("Manager")
            .child(getUid())
    }

    fun staffReference() : DatabaseReference{
        return reference.child("Positions")
            .child("Staff")
    }

    fun staffRefWithUid() : DatabaseReference{
        return reference.child("Positions")
            .child("Staff")
            .child(getUid())
    }

    fun messageRef() : DatabaseReference{
        return reference.child("Messages")
    }
}