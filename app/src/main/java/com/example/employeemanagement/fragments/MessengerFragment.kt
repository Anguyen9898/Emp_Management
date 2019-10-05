package com.example.employeemanagement.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.employeemanagement.R
import com.example.employeemanagement.adapters.Firebase.FireBaseAdapter
import com.example.employeemanagement.adapters.Firebase.GetFirebaseValues
import com.example.employeemanagement.adapters.MessengerUserAdapter
import com.example.employeemanagement.models.MessUserItemDetail
import com.example.employeemanagement.supporters.interfaces.OnMessUserItemClick
import com.google.firebase.database.DataSnapshot

class MessengerFragment : Fragment() {
    private var mess_recycle : RecyclerView? = null
    private lateinit var mList: ArrayList<MessUserItemDetail>
    private var messUserItem: MessUserItemDetail? = null
    private lateinit var mMessAdapter: MessengerUserAdapter

    private val firebaseAdapter = object : FireBaseAdapter(){}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_messenger, container, false)

        mess_recycle = view.findViewById(R.id.mess_user_recycle)
        firebaseAdapter.mContext = context
        setMessagesList()
        return view
    }

    private fun setMessagesList(){
        mList = ArrayList()
        firebaseAdapter.employeesReference()
            .addListenerForSingleValueEvent(object : GetFirebaseValues(context){
                override fun onDataChange(data: DataSnapshot) {
                    data.children.forEach {
                        if(it.child("Account ID").value.toString().trim()
                            == firebaseAdapter.getUid()){
                            Log.i("Messenger", "Don't show current user")
                        }else{
                            val strFullName = it.child("Full Name")
                                .value.toString().trim()
                            val strImgUrl = it.child("ImageUrl")
                                .value.toString().trim()
                            val strAccountId = it.child("Account ID")
                                .value.toString().trim()
                            val strMesId = ""
                            val strMessage = "Click to send new message"

                            messUserItem = MessUserItemDetail(strMesId, strAccountId,
                                strFullName, strMessage, strImgUrl)
                            mList.add(messUserItem!!)

                            val layoutManager = LinearLayoutManager(context)
                            layoutManager.orientation = LinearLayoutManager.VERTICAL
                            mMessAdapter = MessengerUserAdapter(context!!, mList)
                            mess_recycle!!.layoutManager = layoutManager
                            mess_recycle!!.adapter = mMessAdapter
                        }
                    }
                }
            })
    }
}
