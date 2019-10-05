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

class MesssengerAdapter (private var mContext: Context,
                         private var mList: List<MessageDetail>,
                         private var position : Int)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(i: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when(holder.itemViewType) {
            0 -> {
                val holder0 = holder as ViewHolder0
                holder0.txtSend.text = mList[i].message
            }

            2 -> {
                val holder2 = holder as ViewHolder2
                holder2.txtReceive.text = mList[i].message
                Glide.with(mContext).load(mList[i].imgUrl).into(holder.imgReceiver)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder{
        //var view : View? = null
        return when(viewType){
            0 ->ViewHolder0(LayoutInflater.from(mContext).inflate(
                R.layout.sender_item, parent, false))
            2 -> ViewHolder2(LayoutInflater.from(mContext).inflate(
                R.layout.receiver_item, parent, false))
            else -> null!!
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    class ViewHolder0(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtSend = itemView.findViewById<TextView>(R.id.txt_sender_send)!!
    }

    class  ViewHolder2(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imgReceiver = itemView.findViewById<CircleImageView>(R.id.img_receiver)!!
        val txtReceive = itemView.findViewById<TextView>(R.id.txt_receiver_send)!!
    }
}