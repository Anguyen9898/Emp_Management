package com.example.employeemanagement.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.employeemanagement.R
import com.example.employeemanagement.adapters.Firebase.AccountFirebaseAdapter
import com.example.employeemanagement.adapters.Firebase.EmployeesFirebaseAdapter
import com.example.employeemanagement.adapters.Firebase.FireBaseAdapter
import com.example.employeemanagement.adapters.Firebase.GetFirebaseValues
import com.example.employeemanagement.supporters.interfaces.Support
import com.google.firebase.database.*
import kotlin.collections.HashMap

@Suppress("NAME_SHADOWING")
class RegisterActivity : FireBaseAdapter(), Support {
    private lateinit var empId: EditText
    private lateinit var fullname:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var register: Button
    private lateinit var gender: RadioGroup
    private lateinit var male: RadioButton
    private lateinit var feMale: RadioButton
    private lateinit var progressDialog: ProgressDialog
    private var mExtras: Bundle? = null
    private var mIntent: Intent? = null
    private var gen = ""
    private var img = ""
    private var mUid = ""
    private val TAG = "EmailPassword"
    private var employeeId = ""

    private val account = AccountFirebaseAdapter()
    private val employees =
        EmployeesFirebaseAdapter()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mContext = this

        //auth= FirebaseAuth.getInstance()
        mExtras = this.intent.extras
        empId = findViewById(R.id.empId)
        fullname = findViewById(R.id.fullname)
        email = findViewById(R.id.email)
        gender = findViewById(R.id.gender)
        male = findViewById(R.id.male)
        feMale = findViewById(R.id.female)
        password = findViewById(R.id.password)
        register = findViewById(R.id.btn_register)

        getEmployeePos()
        email.setText(mExtras!!.getString("registerEmail"))

        register.setOnClickListener(clickListener)
        gender.setOnCheckedChangeListener(changeListener)
    }

    private val changeListener= RadioGroup.OnCheckedChangeListener{_,_ ->
        setGenderSelector()
    }

    private fun setGenderSelector() {
        when (gender.checkedRadioButtonId) {
            R.id.male -> {
                gen = "Male"
                img = "https://firebasestorage.googleapis.com/v0/b/employeemanagement-b7b5e.appspot.com/o/Male.png?alt=media&token=a48defa8-bbce-491c-83bc-44d1ce60e3e5"
            }
            R.id.female -> {
                gen = "Female"
                img = "https://firebasestorage.googleapis.com/v0/b/employeemanagement-b7b5e.appspot.com/o/Female.png?alt=media&token=032bc270-ccc8-49a9-9d04-c4eb2bab9fa7"
            }
        }
    }

    private val clickListener =View.OnClickListener {
        val strEmpId = empId.text.toString().trim()
        val strFullname = fullname.text.toString().trim()
        val strEmail = email.text.toString().trim()+ "@gmail.com"
        val strPassword = password.text.toString().trim()
        setGenderSelector()

        if (strEmpId.isEmpty() || strEmail.isEmpty()
            || strPassword.isEmpty() || strFullname.isEmpty()){

            //set background color if fields is empty
            setRequiredColor(arrayOf(empId, fullname, email, password))

            Toast.makeText(this@RegisterActivity, "All fill are required!",
                Toast.LENGTH_LONG)
                .show()
        } else if (strPassword.length < 6) {
            Toast.makeText(
                this@RegisterActivity,
                "Password must have at least 6",
                Toast.LENGTH_LONG
            ).show()
            password.selectAll()
        } else if(gen == "") {
            Toast.makeText(
                this@RegisterActivity,
                "Please choose your gender !!", Toast.LENGTH_LONG) .show()
        }
        else {
            progressDialog = ProgressDialog(this@RegisterActivity)
            progressDialog.setMessage("Please wait...")
            progressDialog.show()
            register(strEmpId, strFullname, strPassword, strEmail)
        }
    }

    private fun register(strEmpid: String, strFullname: String, strPassword: String
                         , strEmail: String){
        account.register(strEmail, strPassword)
        .addOnCompleteListener(this@RegisterActivity) { task ->
            if (!task.isSuccessful) {
                // If sign in fails, display a message to the user.
                progressDialog.dismiss()
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(this@RegisterActivity, "Something wrong with this email or password: " + task.exception!!, Toast.LENGTH_LONG)
                    .show()
            } else {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful)

                //firebaseUser = auth.currentUser
                //mUid = firebaseUser!!.uid
                /*reference = FirebaseDatabase.getInstance()
                        .reference
                        .child("Employees")
                        .child(mUid)*/
                val hashMap = HashMap<String, Any?>()
                hashMap["Account ID"] = getUid()
                hashMap["Employee ID"] = strEmpid
                hashMap["Full Name"] = strFullname
                hashMap["Gender"] = gen
                hashMap["ImageUrl"] = img
                hashMap["Email"] = strEmail
                hashMap["Position"] = mExtras!!.getString("Position")
                employees.setEmpValue(hashMap)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        setPositionsData()
                        mIntent = Intent(
                            this@RegisterActivity, MainActivity::class.java)
                        mIntent!!.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        mIntent!!.putExtra("Position", mExtras!!.getString("Position"))
                        startActivity(mIntent)
                    }
                }
            }
        }
    }

    private fun setPositionsData(){
       employeesRefWithUid()
           .addValueEventListener(object : GetFirebaseValues(this){
            override fun onDataChange(data: DataSnapshot) {
                val hashMap = HashMap<String, Any?>()
                hashMap["Employee ID"] = data.child("Employee ID").value.toString()
                hashMap["Full Name"] = data.child("Full Name").value.toString()
                mExtras!!.getString("Position")!!.let {
                    reference.child("Positions")
                        .child(it)
                        .child(getUid())
                }.setValue(hashMap).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this@RegisterActivity, "Updated Successfully"
                            , Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun getEmployeePos(){
        reference.child("Positions")
            .addValueEventListener(object : GetFirebaseValues(this){
                override fun onDataChange(data: DataSnapshot) {
                    when (mExtras!!.getString("Position")) {
                        "Manager" -> empId.setText("M.E 000${data
                            .child("Manager").childrenCount + 1}")
                        "Staff" -> empId.setText("S.E 000${data
                            .child("Staff").childrenCount + 1}")
                        else -> Toast.makeText(
                            applicationContext,
                            "Can't get Position", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

    }
}
