package com.chandra.firebaseapp

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_add.*
import kotlinx.android.synthetic.main.profileactivity.*
import java.io.IOException

class profileactivity:AppCompatActivity() {


    lateinit var database: DatabaseReference
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 22
    var storageReference: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profileactivity)
        database = FirebaseDatabase.getInstance().getReference("LoginData")
        storageReference = FirebaseStorage.getInstance().reference
        saveprofile.setOnClickListener {
            savedetails()
        }
        profilepic.setOnClickListener(){
            SelectImage()
        }


    }

    private fun savedetails() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Setting Up...")
        progressDialog.show()
        val username = username.text.toString().trim()

        if (!TextUtils.isEmpty(username)) {

            val ref: StorageReference =
                storageReference?.child("Profiles")!!.child(filePath!!.lastPathSegment!!)
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                       val uid=FirebaseAuth.getInstance().currentUser!!.uid
                        database.child("Username").setValue(username)
                        database.child("Profile").setValue(it.toString())
                        database.child("Uid").setValue(uid)


                        progressDialog.dismiss()
                        Toast.makeText(this, "Profile is setup sucessfully", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.printStackTrace()}", Toast.LENGTH_LONG).show()
                }


        }
    }

    private fun SelectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        );

        if (requestCode == PICK_IMAGE_REQUEST
            && resultCode == RESULT_OK
            && data != null
            && data.getData() != null
        ) {

            // Get the Uri of data
            filePath = data.getData();
            try {
                //imagebutton.setImageURI(filePath)
                Picasso.get().load(filePath).fit().centerCrop().into(imagebutton)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


}