package com.example.employeemanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.employeemanagement.R
import com.example.employeemanagement.models.MessageDetail
import de.hdodenhof.circleimageview.CircleImageView

class ReceiverAdapter(private var mContext: Context,
                      private var mList: List<MessageDetail> )
    : RecyclerView.Adapter<ReceiverAdapter.ViewHolder>() {
    
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.txtReceive.text = mList[i].message

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.receiver_item
        , parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imgReceiver = itemView.findViewById<CircleImageView>(R.id.img_receiver)!!
        val txtReceive = itemView.findViewById<TextView>(R.id.txt_receiver_send)!!
    }
}