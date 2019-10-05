package com.example.employeemanagement.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeemanagement.R
import com.example.employeemanagement.adapters.Firebase.FireBaseAdapter
import com.example.employeemanagement.adapters.Firebase.GetFirebaseValues
import com.example.employeemanagement.adapters.MesssengerAdapter
import com.example.employeemanagement.models.MessageDetail
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.activity_messenger.*
import kotlinx.android.synthetic.main.message_item.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Suppress("NAME_SHADOWING")
class MessengerActivity : FireBaseAdapter() {
    private var mMessAdapter: MesssengerAdapter? = null
    private var mListMess : ArrayList<MessageDetail>? = ArrayList()

    private var messageId : String? = null
    private var receiverId = ""
    private var imgUrl = ""
    private var messCount = 1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)
        receiverId = intent.extras!!.getString("account")!!.trim()
        imgUrl = intent.extras!!.getString("img")!!.trim()

        send_mess.setOnClickListener(clickListener)
        //send_mess.setOnKeyListener(keyListener)
        setView()
    }

    private val clickListener = View.OnClickListener {view ->
        when(view.id){
            R.id.send_mess -> {
                val strMessage = edt_mess!!.text.toString()
                if(strMessage.isNotEmpty()){
                    //txt_note.visibility = View.GONE
                    setMessageValue(strMessage)
                    edt_mess!!.text = null
                    //mess_recycle.adapter!!.notifyDataSetChanged()
                }
            }
        }
    }

    /*private val keyListener = View.OnKeyListener { view, messCount, keyEvent ->

    }*/

    /**
     *  Set Message ID Methods
     */
    private fun getMessageId(){
        messageRef().addValueEventListener(object : GetFirebaseValues() {
            override fun onDataChange(data: DataSnapshot) {
                if (data.exists()) {
                    if(data.childrenCount ==1L)
                        messCount++
                    else
                        messCount += data.childrenCount
                }
                messageId= "message0$messCount"
            }
        })
    }

    /**
     * Add Messages Value to Employees
     */
    private fun setMessageValue(strMessage: String){
        //getMessageId()
        val messId = messageId
        val dateTime = SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault())
            .format(Calendar.getInstance().time)
        val hashMap = HashMap<String, Any?>()
        hashMap["message"] = strMessage
        hashMap["receiver"] = receiverId
        hashMap["dateTime"] = dateTime
        employeesRefWithUid().apply {
            child("Messages").child("sent")
                .child(messId!!).setValue(hashMap)
                //Set Messages Database Reference
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hashMap["sender"] = getUid()
                        messageRef().child(messId).setValue(hashMap)
                    }
                }
            child("ImageUrl")
                .addValueEventListener(object : GetFirebaseValues(){
                    override fun onDataChange(data: DataSnapshot) {
                        hashMap["senderImgUrl"] = data.value.toString().trim()
                        employeesReference().child(receiverId).child("Messages").child("received")
                            .child(messId).setValue(hashMap)
                    }
                })
        }
    }

    /**
     * Set Recycle View Method
     */
    private fun setView(){
        getMessageId()
        employeesRefWithUid().child("Messages").child("sent")
           .addValueEventListener(object : GetFirebaseValues(){
               override fun onDataChange(data: DataSnapshot) {
                   if(data.exists()){
                       txt_note.visibility = View.GONE
                       mListMess!!.add(MessageDetail("", data.child(messageId!!)
                           .child("message").value.toString().trim()))
                       mMessAdapter = MesssengerAdapter(this@MessengerActivity
                           , mListMess!!, 0)

                       val layoutManager = LinearLayoutManager(this@MessengerActivity)
                       layoutManager.orientation = LinearLayoutManager.VERTICAL
                       mess_recycle!!.layoutManager = layoutManager
                       mess_recycle!!.adapter = mMessAdapter
                   }
               }
           })
        employeesRefWithUid().child("Messages").child("received")
            .addValueEventListener(object : GetFirebaseValues(){
                override fun onDataChange(data: DataSnapshot) {
                    if(data.exists()){
                        txt_note.visibility = View.GONE
                        data.children.forEach { messageId ->
                            if(messageId.child("sender").value.toString().trim()
                            == receiverId){
                                mListMess!!.add(MessageDetail(
                                    messageId.child("senderImgUrl").value.toString().trim()
                                    , messageId.child("message").value.toString().trim())
                                )

                                mMessAdapter = MesssengerAdapter(this@MessengerActivity
                                    , mListMess!!, 2)

                               if(mMessAdapter!!.itemCount == 1){
                                   val layoutManager = LinearLayoutManager(this@MessengerActivity)
                                   layoutManager.orientation = LinearLayoutManager.VERTICAL
                                   mess_recycle!!.layoutManager = layoutManager
                                   mess_recycle!!.adapter = mMessAdapter
                               }else{
                                   mess_recycle_item.adapter!!.notifyDataSetChanged()
                               }
                            }
                        }
                    }
                }
            })

    }

}
