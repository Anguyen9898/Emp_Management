package com.example.employeemanagement.adapters.Firebase

import android.annotation.SuppressLint
import com.google.android.gms.tasks.Task

@SuppressLint("Registered")
class MassageFirebaseAdapter(var mesId : String): FireBaseAdapter() {

    private var setValueResult : Task<Void>? = null
    private var updateValueResult : Task<Void>? = null

    fun setEmpValue(hashMap: HashMap<String, Any?>) : Task<Void>?{
        try {
            setValueResult = messageRef().child(mesId).setValue(hashMap)
        }
        catch (ex : Exception){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        finally {
            messageRef().child(mesId).onDisconnect()
        }
        return setValueResult
    }

    fun updateEmpValue(hashMap: HashMap<String, Any?>) : Task<Void>?{
        try {
            updateValueResult = messageRef().child(mesId).updateChildren(hashMap)
        }
        catch (ex : Exception){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        finally {
            messageRef().onDisconnect()
        }
        return updateValueResult
    }
}