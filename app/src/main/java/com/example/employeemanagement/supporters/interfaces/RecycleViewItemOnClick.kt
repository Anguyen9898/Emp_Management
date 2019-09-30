package com.example.employeemanagement.supporters.interfaces

import android.view.View

interface RecycleViewItemOnClick  {
    fun onClick(view: View, position: Int, isClicked: Boolean)

}