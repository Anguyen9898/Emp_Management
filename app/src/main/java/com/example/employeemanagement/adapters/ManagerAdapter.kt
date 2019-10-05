package com.example.employeemanagement.adapters

import com.example.employeemanagement.adapters.Firebase.FireBaseAdapter
import com.example.employeemanagement.supporters.interfaces.Firebase
import com.google.android.gms.tasks.Task
import java.lang.Exception

class ManagerAdapter : FireBaseAdapter(), Firebase{

    private var setManagerValueResult : Task<Void>? = null
    private var updateManagerValueResult : Task<Void>? = null

    fun setManagerValue(hashMap: HashMap<String, Any?>) : Task<Void>?{
        try {
            setManagerValueResult = managerRefWithUid().setValue(hashMap)
        }
        catch (ex : Exception){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        finally {
            managerRefWithUid().onDisconnect()
        }
        return setManagerValueResult
    }

    fun updateManagerValue(hashMap: HashMap<String, Any?>) : Task<Void>?{
        try {
            updateManagerValueResult = managerRefWithUid().updateChildren(hashMap)
        }
        catch (ex : Exception){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        finally {
            managerRefWithUid().onDisconnect()
        }
        return updateManagerValueResult
    }
}