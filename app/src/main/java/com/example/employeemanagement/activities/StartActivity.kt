package com.example.employeemanagement.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.employeemanagement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class StartActivity : AppCompatActivity() {

    private lateinit var btn_Staff: Button
    private lateinit var btn_Manager: Button
    private var currentUser: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        // CheckPermission if user is signed in (non-null) and update UI accordingly
        currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val mIntent = Intent(this@StartActivity, MainActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(mIntent)
            finish()
        } else {
            btn_Staff = findViewById(R.id.btn_Staff)
            btn_Manager = findViewById(R.id.btn_Manager)
            btn_Staff.setOnClickListener(click)
            btn_Manager.setOnClickListener(click)
        }
    }

    private val click = View.OnClickListener {view->
        val mIntent = Intent(this@StartActivity,
            LoginActivity::class.java)
        when(view.id){
            R.id.btn_Manager -> mIntent.putExtra("Position", "Manager")
            R.id.btn_Staff -> mIntent.putExtra("Position","Staff")
        }
        this.startActivity(mIntent)
    }
}
