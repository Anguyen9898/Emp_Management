package com.example.employeemanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.employeemanagement.R
import com.example.employeemanagement.adapters.Firebase.FireBaseAdapter
import com.example.employeemanagement.adapters.Firebase.GetFirebaseValues
import com.example.employeemanagement.models.MessageDetail
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.activity_messenger.*
import kotlinx.android.synthetic.main.message_item.*

/*class MessengerAdapter (private var mContext: Context? = null,
                        private var messageId: String? = null,
                        private var receiverId: String? = null,
                        private var mListMess: ArrayList<RecyclerView>)
    : RecyclerView.Adapter<MessengerAdapter.ViewHolder>() {

    private var mListSend: ArrayList<String>? = ArrayList()
    private var mListReceive: ArrayList<MessageDetail>? = ArrayList()
    private var mReceiverAdapter: ReceiverAdapter? = null
    private var mSederAdapter: MesssengerAdapter? = null
    private var firebaseAdapter= object : FireBaseAdapter(){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.message_item
            , parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mListMess.size
    }

    /*override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        firebaseAdapter.employeesRefWithUid()
            .child("Messages").child("sent")
            .addValueEventListener(object : GetFirebaseValues(){
                override fun onDataChange(data: DataSnapshot) {
                    if(data.exists()){
                        holder.txt_note.visibility = View.GONE
                        mListSend!!.add(data.child(messageId!!)
                            .child("message").value.toString().trim())

                        val layoutManager = LinearLayoutManager(mContext)
                        layoutManager.orientation = LinearLayoutManager.VERTICAL
                        mSederAdapter = MesssengerAdapter(mContext!!
                            , mListSend!!)
                        holder.mess_recycle.layoutManager = layoutManager
                        holder.mess_recycle.adapter = mSederAdapter
                        mListMess.add(holder.mess_recycle)
                    }
                }
            })
        firebaseAdapter.employeesRefWithUid()
            .child("Messages").child("received")
            .addValueEventListener(object : GetFirebaseValues(){
                override fun onDataChange(data: DataSnapshot) {
                    if(data.exists()){
                        holder.txt_note.visibility = View.GONE
                        data.children.forEach { messageId ->
                            if(messageId.child("sender").value.toString().trim()
                                == receiverId){
                                mListReceive!!.add(MessageDetail(
                                    messageId.child("senderImgUrl").value.toString().trim()
                                    , messageId.child("message").value.toString().trim())
                                )

                                val layoutManager = LinearLayoutManager(mContext)
                                layoutManager.orientation = LinearLayoutManager.VERTICAL
                                mReceiverAdapter = ReceiverAdapter(mContext!!
                                    ,mListReceive!!)
                                holder.mess_recycle.layoutManager = layoutManager
                                holder.mess_recycle.adapter = mReceiverAdapter
                                mListMess.add(holder.mess_recycle)
                            }
                        }
                    }
                }
            })*/

}

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txt_note = itemView.findViewById<TextView>(R.id.txt_note)!!
        val mess_recycle = itemView.findViewById<RecyclerView>(R.id.mess_recycle_item)!!
    }
}*/