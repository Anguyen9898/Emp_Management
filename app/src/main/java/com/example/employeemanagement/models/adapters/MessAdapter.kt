package com.example.employeemanagement.models.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.employeemanagement.R
import de.hdodenhof.circleimageview.CircleImageView

class MessAdapter (private var mContext: Context,
                   private var mListName: List<String>,
                   private var mListMess: List<String>,
                   private var mListImgUrl: List<String>)
    : RecyclerView.Adapter<MessAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.name.text = mListName[i]
        holder.mess.text = mListMess[i]
        Glide.with(mContext).load(mListImgUrl[i]).into(holder.img)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.mess_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mListName.size or mListMess.size or mListImgUrl.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.txt_name)!!
        val mess = itemView.findViewById<TextView>(R.id.txt_mess)!!
        val img = itemView.findViewById<CircleImageView>(R.id.image_mess)
    }
}