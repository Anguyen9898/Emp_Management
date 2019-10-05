package com.example.employeemanagement.adapters

import android.annotation.SuppressLint
import com.example.employeemanagement.adapters.Firebase.FireBaseAdapter
import com.example.employeemanagement.supporters.interfaces.Firebase
import com.google.android.gms.tasks.Task
import java.lang.Exception

@SuppressLint("Registered")
class StaffAdapter : FireBaseAdapter(), Firebase {

    private var setStaffValueResult : Task<Void>? = null
    private var updateStaffValueResult : Task<Void>? = null

    fun setStaffValue(hashMap: HashMap<String, Any?>) : Task<Void>?{
        try {
            setStaffValueResult = staffRefWithUid().setValue(hashMap)
        }
        catch (ex : Exception){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        finally {
            staffRefWithUid().onDisconnect()
        }
        return setStaffValueResult
    }

    fun updateStaffValue(hashMap: HashMap<String, Any?>): Task<Void>?{
        try {
            updateStaffValueResult = staffRefWithUid().updateChildren(hashMap)
        }
        catch (ex : Exception){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        finally {
            staffRefWithUid().onDisconnect()
        }
        return  updateStaffValueResult
    }
}