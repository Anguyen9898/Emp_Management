package com.example.employeemanagement.models.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.employeemanagement.R
import com.example.employeemanagement.models.EmployeeDetail
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class ProfileAdapter(private var mContext: Context,
                     private var mListField: List<String>,
                     private var mListValue: List<String>)
    : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.info_item, viewGroup,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.field.text = mListField[i]
        holder.value.text = mListValue[i]
    }

    override fun getItemCount(): Int {
        return mListValue.size or mListField.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val field = itemView.findViewById<TextView>(R.id.field)!!
        val value = itemView.findViewById<TextView>(R.id.value)!!
    }

}