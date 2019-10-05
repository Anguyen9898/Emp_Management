package com.example.employeemanagement.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.employeemanagement.R
import com.example.employeemanagement.adapters.Firebase.AccountFirebaseAdapter
import com.example.employeemanagement.adapters.Firebase.EmployeesFirebaseAdapter
import com.example.employeemanagement.adapters.Firebase.FireBaseAdapter
import com.example.employeemanagement.adapters.Firebase.GetFirebaseValues
import com.example.employeemanagement.supporters.interfaces.Support
import com.example.employeemanagement.view.LoginView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : FireBaseAdapter(), LoginView,
    Support {

    private lateinit var email: EditText
    private lateinit var idHeader: TextView
    private lateinit var empId: EditText
    private lateinit var password:EditText
    private lateinit var login: Button
    private lateinit var register: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    //private lateinit var reference: DatabaseReference
    var mBundle: Bundle? =null
    var position= ""

    private val account = AccountFirebaseAdapter()
    private val employees =
        EmployeesFirebaseAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mContext = this

        idHeader= findViewById(R.id.id_header)
        mBundle = this.intent.extras
        position = mBundle!!.getString("Position").toString()
        when(position)
        {
            "Manager"->idHeader.text = "M.E"
            "Staff"->idHeader.text = "S.E"
        }

        email = findViewById(R.id.email)
        empId = findViewById(R.id.empId)
        password = findViewById(R.id.password)
        login = findViewById(R.id.btn_login)
        register = findViewById(R.id.btn_register)
        //auth = FirebaseAuth.getInstance()
        login.setOnClickListener {
            val strEmail = email.text.toString().trim() + "@gmail.com"
            val strPassword = password.text.toString().trim()
            val strID = idHeader.text.toString().trim()+ " " + empId.text.toString().trim()

            if (strID.isEmpty() || strEmail.isEmpty() || strPassword.isEmpty()) {
                //set background color if fields is empty
                setRequiredColor(arrayOf(empId, email, password))

                Toast.makeText(this@LoginActivity, "All fill are required!", Toast.LENGTH_LONG)
                    .show()
            } else {
                login(strEmail, strPassword, strID)
            }
        }

        register.setOnClickListener {
            val mIntent = Intent(this, RegisterActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("Position", position)
            mBundle.putString("registerEmail", email.text.toString().trim())
            mIntent.putExtras(mBundle)
            startActivity(mIntent)
        }
    }

    private fun login(strEmail: String, strPassword: String, strID: String) {
        progressDialog = ProgressDialog(this@LoginActivity)
        progressDialog.setMessage("Please wait...")
        progressDialog.show()

        account.login(strEmail, strPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                employeesRefWithUid().addValueEventListener(object : GetFirebaseValues(this) {
                    override fun onDataChange(data: DataSnapshot) {
                        progressDialog.dismiss()

                        if(strID != data.child("Employee ID").value.toString()){
                            AlertDialog.Builder(this@LoginActivity).apply {
                                //Setting Dialog Title
                                setTitle("Wrong ID")
                                //Setting Dialog Message
                                setMessage("Your Employee ID is wrong. Please modify to a right ID and try again!")
                                //On pressing Setting button
                                setPositiveButton("OK"){dialog, _->
                                    dialog.dismiss()
                                }.show()
                            }
                        }else{
                            val mIntent = Intent(this@LoginActivity, MainActivity::class.java)
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            mIntent.putExtra("Position", position)
                            startActivity(mIntent)
                            finish()
                        }
                    }
                })
            } else {
                progressDialog.dismiss()
                AlertDialog.Builder(this@LoginActivity).apply {
                    //Setting Dialog Title
                    setTitle("Authentication failed!")
                    //Setting Dialog Message
                    setMessage("Your Email or Password is wrong. Please modify it and try again!")
                    //On pressing Setting button
                    setPositiveButton("OK"){dialog, _->
                        dialog.dismiss()
                    }
                }.show()
            }
        }
    }

    override fun loginSucess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loginFail(message: String) {
        Toast.makeText(this, "Fail!", Toast.LENGTH_LONG).show()
    }
}
