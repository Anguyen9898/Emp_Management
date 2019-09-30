package com.example.employeemanagement.models.adapters

import android.annotation.SuppressLint
import com.example.employeemanagement.supporters.interfaces.Firebase
import com.google.android.gms.tasks.Task

@SuppressLint("Registered")
class EmployeesAdapter : FireBaseAdapter(), Firebase{

    private var setValueResult : Task<Void>? = null
    private var updateValueResult : Task<Void>? = null


    fun setEmpValue(hashMap: HashMap<String, Any?>) : Task<Void>?{
        try {
            setValueResult = employeesRefWithUid().setValue(hashMap)
        }
        catch (ex : Exception){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        finally {
            employeesRefWithUid().onDisconnect()
        }
        return setValueResult
    }

    fun updateEmpValue(hashMap: HashMap<String, Any?>) : Task<Void>?{
        try {
            updateValueResult = employeesRefWithUid().updateChildren(hashMap)
        }
        catch (ex : Exception){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        finally {
            employeesRefWithUid().onDisconnect()
        }
        return updateValueResult
    }
}