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
import com.example.employeemanagement.models.adapters.FireBaseAdapter
import com.example.employeemanagement.models.adapters.GetValues
import com.example.employeemanagement.models.adapters.MessAdapter
import com.google.firebase.database.DataSnapshot

class MessengerFragment : Fragment() {
    private var mess_recycle : RecyclerView? = null
    private lateinit var mNameList: ArrayList<String>
    private lateinit var mMessList: ArrayList<String>
    private lateinit var mImgUrlList: ArrayList<String>
    private lateinit var mMessAdapter: MessAdapter

    private val firebaseAdapter = object : FireBaseAdapter(){}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_messenger, container, false)

        mess_recycle = view.findViewById(R.id.mess_recycle)
        firebaseAdapter.mContext = context
        setMessengersList()
        return view
    }

    fun setMessengersList(){
        mNameList = ArrayList()
        mMessList = ArrayList()
        mImgUrlList = ArrayList()

        firebaseAdapter.employeesReference()
            .addValueEventListener(object : GetValues(context){
                override fun onDataChange(data: DataSnapshot) {
                    data.children.forEach {
                        if(it.child("Account ID").value.toString().trim()
                            == firebaseAdapter.getUid()){
                            Log.i("Messenger", "Don't show current user")
                        }else{
                            mNameList.add(it.child("Full Name").value.toString().trim())
                            mImgUrlList.add(it.child("ImageUrl").value.toString().trim())

                            if (it.child("Messenger").exists())
                                mMessList.add(it.child("Messenger").value.toString().trim())
                            else
                                mMessList.add("Click to send a new message")
                        }
                    }
                    setRecycleView(mess_recycle, mNameList, mMessList, mImgUrlList)
                }
            })
    }

    /**
     * Set RecycleView layout method
     */
    fun setRecycleView(view: RecyclerView?, list1: ArrayList<String>
                       , list2: ArrayList<String>, list3: ArrayList<String>){
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mMessAdapter = MessAdapter(context!!, list1, list2, list3)
        view!!.layoutManager = layoutManager
        view.adapter = mMessAdapter
    }

}
