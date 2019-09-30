package com.example.employeemanagement.models.adapters

import android.annotation.SuppressLint
import com.example.employeemanagement.supporters.interfaces.Firebase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

@SuppressLint("Registered")
class AccountAdapter : FireBaseAdapter(), Firebase {
    private var registerResult: Task<AuthResult>? = null
    private var loginResult: Task<AuthResult>? = null

    fun register(email: String, pass: String) : Task<AuthResult>{
        try {
            registerResult = firebaseAuth.createUserWithEmailAndPassword(email, pass)
        }
        catch (ex : FirebaseAuthWebException){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        catch (ex : FirebaseAuthException){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        return registerResult!!
    }

    fun login(email: String, pass: String) : Task<AuthResult>{
        try {
            loginResult = firebaseAuth.signInWithEmailAndPassword(email, pass)
        }
        catch (ex : FirebaseAuthWebException){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        catch (ex : FirebaseAuthException){
            setAlertDialog(mContext, "Error!", ex.message!!).apply {
                setNegativeButton("OK"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
        return loginResult!!
    }
}