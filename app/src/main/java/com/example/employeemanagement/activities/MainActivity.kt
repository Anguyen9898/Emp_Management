package com.example.employeemanagement.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.employeemanagement.R
import com.example.employeemanagement.fragments.ManagerHomeFrag
import com.example.employeemanagement.fragments.MessengerFragment
import com.example.employeemanagement.fragments.ProfileFragment
import com.example.employeemanagement.fragments.StaffHomeFrag
import com.example.employeemanagement.adapters.Firebase.FireBaseAdapter
import com.example.employeemanagement.adapters.Firebase.GetFirebaseValues
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot

class MainActivity : FireBaseAdapter() {

    private var bottomNavigationView: BottomNavigationView? = null
    private var selectedFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        this.bottomNavigationView!!.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        val intent = intent.extras
        if (intent != null) {
            val publisher = intent.getString("publisherid")

            val editor = getSharedPreferences("PREPS", MODE_PRIVATE).edit()
            editor.putString("profileid", publisher)
            editor.apply()
            setHomeFragment()
        } else {
            setHomeFragment()
        }
    }

    private val navigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {menuItem->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    setHomeFragment()
                }

                R.id.nav_mess -> selectedFragment = MessengerFragment()

                //R.id.nav_notify -> selectedFragment = NotificationFragment()
                R.id.nav_profile -> {
                    val editor = getSharedPreferences("PREPS", MODE_PRIVATE).edit()
                    editor.putString("profileid", FirebaseAuth.getInstance().currentUser!!.uid)
                    editor.apply()
                    selectedFragment = ProfileFragment()
                }
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment!!)
                    .commit()
            }
                true
        }

    private fun setHomeFragment(){
       employeesRefWithUid().child("Position")
            .addValueEventListener(object : GetFirebaseValues(this){
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    when (dataSnapshot.value.toString()) {
                        "Manager" -> selectedFragment = ManagerHomeFrag()
                        "Staff" -> selectedFragment = StaffHomeFrag()
                    }
                    if (selectedFragment != null) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment!!)
                            .commit()
                    }
                }
            })
    }
}
