package com.example.employeemanagement.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.employeemanagement.R
import com.example.employeemanagement.activities.EditProfileActivity
import com.example.employeemanagement.adapters.Firebase.FireBaseAdapter
import com.example.employeemanagement.adapters.Firebase.GetFirebaseValues
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView

import com.example.employeemanagement.adapters.ProfileAdapter
import com.example.employeemanagement.supporters.interfaces.Support
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class ProfileFragment : Fragment(), Support {
    private var image_profile : CircleImageView? = null
    private var basicInfo : RecyclerView? = null
    private var moreInfo : RecyclerView? = null
    private var btn_edit : Button? = null

    private lateinit var mProfileAdapte: ProfileAdapter
    private lateinit var mBasicField: ArrayList<String>
    private lateinit var mBasicValue: ArrayList<String>
    private lateinit var mMoreField: ArrayList<String>
    private lateinit var mMoreValue: ArrayList<String>

    private var mImageUri: Uri? = null
    private var uploadTask: UploadTask? = null

    private val firebaseAdapter = object : FireBaseAdapter(){}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_profile, container, false)

        image_profile = view.findViewById(R.id.image_profile)
        basicInfo = view.findViewById(R.id.basicInfo)
        moreInfo = view.findViewById(R.id.moreInfo)
        btn_edit = view.findViewById(R.id.btn_editProfile)

        btn_edit!!.setOnClickListener(clickListener)
        image_profile!!.setOnClickListener(clickListener)
        setProfile()
        return view
    }

    /**
     * Set RecycleView data method
     */
    private fun setProfile(){
        firebaseAdapter.employeesRefWithUid()
            .addValueEventListener(object : GetFirebaseValues(context){
                override fun onDataChange(data: DataSnapshot) {
                    mBasicField = ArrayList()
                    mBasicValue = ArrayList()
                    mMoreField = ArrayList()
                    mMoreValue = ArrayList()

                    //Set Image's Profile
                    Glide.with(context!!).load(data.child("ImageUrl")
                        .value.toString()).into(image_profile!!)

                    //Get data from firebase
                    for (i in data.children){
                        val strFiled = i.key!!.trim()
                        val strValue = i.value.toString().trim()
                        if (strFiled == "ImageUrl" || strFiled == "Messages"){
                            Log.i("Profile", "Do nothing with Image Url && Messages!")
                        }
                        else if(strFiled == "Employee ID" ||strFiled == "Full Name" ||
                            strFiled == "Gender" || strFiled == "Position") {
                            mBasicField.add("$strFiled:")
                            mBasicValue.add(strValue)

                        }else{
                            mMoreField.add("$strFiled:")
                            mMoreValue.add(strValue)
                        }
                    }
                    //Set profile
                    setRecycleView(basicInfo, mBasicField, mBasicValue)
                    //Set more info
                    setRecycleView(moreInfo, mMoreField, mMoreValue)
                }
            })
    }

    /**
     * Set RecycleView layout method
     */
    fun setRecycleView(view: RecyclerView?, list1: ArrayList<String>, list2: ArrayList<String>){
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mProfileAdapte = ProfileAdapter(context!!, list1, list2)
        view!!.layoutManager = layoutManager
        view.adapter = mProfileAdapte
    }

    /**
     * Click event
     */
    private val clickListener = View.OnClickListener { view->
        when(view.id){
            R.id.btn_editProfile -> {
                startActivity(Intent(context, EditProfileActivity::class.java))
                mProfileAdapte.notifyDataSetChanged()
            }
            R.id.image_profile -> {
                CropImage.activity()
                    .setAspectRatio(1,1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(context as Activity)
                mProfileAdapte.notifyDataSetChanged()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && requestCode == Activity.RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            mImageUri = result.uri
            uploadImage()
        } else {
            Toast.makeText(context, "Something gone wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(){
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading...")
        progressDialog.show()
        val storage = FirebaseStorage.getInstance().getReference("uploads")
        if(mImageUri != null){
            val fileReference = storage.child(
                "${System.currentTimeMillis()}. ${getFileExtension(mImageUri!!)}")

                uploadTask = fileReference.putFile(mImageUri!!)
                uploadTask!!.continueWithTask<Uri>{ task ->
                    if(!task.isSuccessful){
                        throw task.exception!!
                    }
                    return@continueWithTask fileReference.downloadUrl
                }.apply {

                    addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result as Uri
                            val mUrl = downloadUri.toString()

                            val hashMap = HashMap<String, Any?>()
                            hashMap["ImageUrl"] = mUrl

                            FirebaseDatabase.getInstance().reference.child("Employees")
                                .child(FirebaseAuth.getInstance().uid!!)
                                .updateChildren(hashMap)
                            progressDialog.dismiss()
                        } else {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    addOnFailureListener{exception ->
                        Toast.makeText(context, exception.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        } else{
            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = activity!!.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}
