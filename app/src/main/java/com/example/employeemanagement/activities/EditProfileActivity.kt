package com.example.employeemanagement.activities

import android.app.ProgressDialog
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import com.example.employeemanagement.R
import com.example.employeemanagement.models.adapters.FireBaseAdapter
import com.example.employeemanagement.models.adapters.GetValues
import com.example.employeemanagement.supporters.interfaces.Support
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.rengwuxian.materialedittext.MaterialEditText
import kotlin.collections.HashMap

class EditProfileActivity : FireBaseAdapter(), Support {

    private lateinit var changeMain : TextView
    private lateinit var changeEmail : TextView
    private lateinit var changePass : TextView
    private lateinit var changeInfo : TextView
    private lateinit var saveMain : TextView
    private lateinit var saveEmail : TextView
    private lateinit var savePass : TextView
    private lateinit var saveInfo : TextView

    private lateinit var close : ImageView

    private lateinit var fullName : MaterialEditText
    private lateinit var gender : MaterialEditText
    private lateinit var email : MaterialEditText
    private lateinit var newPass : MaterialEditText
    private lateinit var confirmPass : MaterialEditText
    private lateinit var birthday : MaterialEditText
    private lateinit var phoneNum : MaterialEditText
    private lateinit var certificate : MaterialEditText

    private lateinit var description : EditText
    private lateinit var progressDialog: ProgressDialog

    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        progressDialog= ProgressDialog(this)

        changeMain = findViewById(R.id.txt_main)
        changeEmail = findViewById(R.id.txt_change_email)
        changePass = findViewById(R.id.txt_change_pass)
        changeInfo = findViewById(R.id.txt_info)

        changeMain.underLine()
        changeEmail.underLine()
        changePass.underLine()
        changeInfo.underLine()

        fullName = findViewById(R.id.fullname)
        gender = findViewById(R.id.gender)

        saveMain = findViewById(R.id.save_main)
        saveEmail = findViewById(R.id.save_email)
        savePass = findViewById(R.id.save_pass)
        saveInfo = findViewById(R.id.save_info)

        email = findViewById(R.id.email)
        confirmPass = findViewById(R.id.confirm_password)
        newPass = findViewById(R.id.new_password)
        birthday = findViewById(R.id.birthday)
        phoneNum = findViewById(R.id.phone_num)
        certificate = findViewById(R.id.certificate)
        description = findViewById(R.id.description)

        close = findViewById(R.id.close)

        changeMain.setOnClickListener(clickListener)
        changeEmail.setOnClickListener(clickListener)
        changePass.setOnClickListener(clickListener)
        changeInfo.setOnClickListener(clickListener)

        saveMain.setOnClickListener(clickListener)
        saveEmail.setOnClickListener(clickListener)
        savePass.setOnClickListener(clickListener)
        saveInfo.setOnClickListener(clickListener)

        certificate.onFocusChangeListener = focusListener
        description.onFocusChangeListener = focusListener
        close.setOnClickListener(clickListener)

        i = findViewById<LinearLayout>(R.id.main_layout).layoutParams.height
    }

    private val clickListener = View.OnClickListener { view ->
        when(view.id){
            R.id.txt_main -> openOptions(findViewById(R.id.main_layout))
            R.id.txt_change_email -> openOptions(findViewById(R.id.email_layout))
            R.id.txt_change_pass -> openOptions(findViewById(R.id.pass_layout))
            R.id.txt_info -> {
                val layout = findViewById<LinearLayout>(R.id.info_layout)
                val params = layout.layoutParams
                if(params.height == i){
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    findViewById<LinearLayout>(R.id.main_layout).visibility = View.GONE
                    findViewById<LinearLayout>(R.id.email_layout).visibility = View.GONE
                    findViewById<LinearLayout>(R.id.pass_layout).visibility = View.GONE
                }
                else if(params.height == ViewGroup.LayoutParams.WRAP_CONTENT){
                    params.height = i
                    findViewById<LinearLayout>(R.id.main_layout).visibility = View.VISIBLE
                    findViewById<LinearLayout>(R.id.email_layout).visibility = View.VISIBLE
                    findViewById<LinearLayout>(R.id.pass_layout).visibility = View.VISIBLE
                }

                layout.layoutParams = params
            }

            R.id.save_main -> {
                progressDialog.setMessage("Please wait...")
                progressDialog.show()

                val strFullName = fullName.text.toString().trim()
                val strGender = gender.text.toString().trim()

                val hasMap = HashMap<String, Any?>()
                if (strFullName.isNotEmpty()) {
                    hasMap["Full Name"] = strFullName

                    employeesRefWithUid().child("Position")
                        .addValueEventListener(object : GetValues(this) {
                            override fun onDataChange(data: DataSnapshot) {
                                reference.child("Positions")
                                    .child(data.value.toString().trim())
                                    .child(FirebaseAuth.getInstance().uid!!)
                                    .updateChildren(hasMap)
                            }
                        })
                }
                if (strGender.isNotEmpty())
                    hasMap["Gender"] = strGender
                employeesRefWithUid().updateChildren(hasMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Success", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }

            R.id.save_email -> {
                progressDialog.setMessage("Please wait...")
                progressDialog.show()

                val strEmail = email.text.toString().trim()
                val hashMap = HashMap<String, Any?>()
                if (strEmail.isNotEmpty()) {
                    hashMap["Email"] = strEmail
                    firebaseAuth.currentUser!!.updateEmail(strEmail)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                progressDialog.dismiss()
                                Toast.makeText(this, "Success", Toast.LENGTH_LONG)
                                    .show()

                                employeesRefWithUid().child("Email")
                                    .updateChildren(hashMap)
                            }
                        }

                }
            }

            R.id.save_pass -> {
                progressDialog.setMessage("Please wait...")
                progressDialog.show()

                val strConfirmPass = confirmPass.text.toString().trim()
                val strNewPass = newPass.text.toString().trim()
                if (strConfirmPass.isEmpty() || strNewPass.isEmpty()) {
                    setRequiredColor(arrayOf(confirmPass, newPass))
                    setAlertDialog(
                        this, "Password",
                        "You have to fill both of new and confirm password if you want to change your password"
                    )

                        .setNegativeButton("OK") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .show()
                } else if (strConfirmPass != strNewPass) {
                    setAlertDialog(
                        this, "Password",
                        "Confirm password is wrong"
                    )

                        .setNegativeButton("OK") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .show()
                } else {
                    firebaseAuth.currentUser!!.updatePassword(strNewPass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                progressDialog.dismiss()
                                Toast.makeText(this, "Success", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                }
            }

            R.id.save_info -> {
                progressDialog.setMessage("Please wait...")
                progressDialog.show()
                val strBirthday = birthday.text.toString().trim()
                val strPhoneNum = phoneNum.text.toString().trim()
                val strDescript = description.text.toString().trim()
                val strCer = certificate.text.toString().trim()
                val hashMap = HashMap<String, Any?>()

                if (strBirthday.isNotEmpty())
                    hashMap["Day of birth"] = strBirthday
                if (strPhoneNum.isNotEmpty())
                    hashMap["Phone Number"] = strPhoneNum
                if (strCer.isNotEmpty())
                    hashMap["Qualification"] = strCer
                if (strDescript.isNotEmpty())
                    hashMap["Description"] = strDescript

                employeesRefWithUid().updateChildren(hashMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Success", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }

            R.id.close -> {
                onBackPressed()
            }
        }
    }

    private fun openOptions(layout: LinearLayout){
        val params = layout.layoutParams
        if(params.height == i)
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        else if(params.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            params.height = i
            layout.children.forEach {
                it.isEnabled = false
            }
        }

        layout.layoutParams = params
    }

    private fun TextView.underLine(){
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    private val focusListener = View.OnFocusChangeListener{ view,  hasFocus ->
        when (view.id){
            R.id.certificate -> {
                if(hasFocus){
                    birthday.visibility = View.GONE
                    phoneNum.visibility = View.GONE
                }
                else{
                    birthday.visibility = View.VISIBLE
                    phoneNum.visibility = View.VISIBLE
                }
            }
            R.id.description -> {
                if(hasFocus){
                    birthday.visibility = View.GONE
                    phoneNum.visibility = View.GONE
                    certificate.visibility = View.GONE
                }
                else{
                    birthday.visibility = View.VISIBLE
                    phoneNum.visibility = View.VISIBLE
                    certificate.visibility = View.VISIBLE
                }
            }
        }
    }

    /*private fun isTimeOut() : Boolean{
        val t = Timer()
        t.schedule(timerTask {
            progressDialog.setMessage("Please wait...")
            progressDialog.show()
        }, 30000)
        return true
    }*/
}
