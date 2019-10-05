package com.example.employeemanagement.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.employeemanagement.R
import com.example.employeemanagement.activities.MessengerActivity
import com.example.employeemanagement.models.MessUserItemDetail
import com.example.employeemanagement.supporters.interfaces.OnMessUserItemClick
import de.hdodenhof.circleimageview.CircleImageView

class MessengerUserAdapter (private var mContext: Context,
                            private var mList: List<MessUserItemDetail>)
    : RecyclerView.Adapter<MessengerUserAdapter.ViewHolder>(), OnMessUserItemClick{

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.name.text = mList[i].fullName
        holder.mess.text = mList[i].meassage
        holder.uid.text = mList[i].accountId
        Glide.with(mContext).load(mList[i].imgUrl).into(holder.img)
        holder.item.setOnClickListener{
            val intent = Intent(mContext, MessengerActivity::class.java)
            val extras = Bundle()
            extras.putString("account", getAccountId(mList[i].accountId))
            extras.putString("img", getImgUrl(mList[i].imgUrl))
            intent.putExtras(extras)
            mContext.startActivity(intent)
        }
    }

    override fun getImgUrl(url: String): String {
        return url
    }

    override fun getAccountId(id: String) : String {
       return id
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.mess_user_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.txt_name)!!
        val mess = itemView.findViewById<TextView>(R.id.txt_mess)!!
        val img = itemView.findViewById<CircleImageView>(R.id.image_mess)!!
        val item = itemView.findViewById<LinearLayout>(R.id.item)
        val uid = itemView.findViewById<TextView>(R.id.txt_Uid)
    }
}