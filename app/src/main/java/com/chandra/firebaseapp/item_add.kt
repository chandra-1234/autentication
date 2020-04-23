package com.chandra.firebaseapp

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_add.*
import java.io.IOException

class item_add:AppCompatActivity() {

    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 22
    var storageReference: StorageReference? = null
    lateinit var databaseReference: DatabaseReference
    lateinit var lateuri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_add)


        storageReference= FirebaseStorage.getInstance().reference
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").push()
        imagebutton.setOnClickListener() {
            SelectImage();

        }
        save.setOnClickListener(){
            //var Title=edt2.text.toString()
            //var Description=edt1.text.toString()
            saveimage()
        }
        pressback.setOnClickListener {
            backpress()
        }
    }

    private fun backpress() {
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    private fun saveimage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()
        val Title =Title.text.toString().trim()

        if (!TextUtils.isEmpty(Title) ) {

            val ref: StorageReference = storageReference?.child("Images")!!.child(filePath!!.lastPathSegment!!)
            ref.putFile(filePath!!)
                .addOnSuccessListener{
                    ref.downloadUrl.addOnSuccessListener {
                        databaseReference.child("Title").setValue(Title)
                        databaseReference.child("Image").setValue(it.toString())


                        progressDialog.dismiss()
                        Toast.makeText(this,"uploaded sucessfully",Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this,"${it.printStackTrace()}", Toast.LENGTH_LONG).show()
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
            try{
                //imagebutton.setImageURI(filePath)
                Picasso.get().load(filePath).fit().centerCrop().into(imagebutton)
            }
            catch (e: IOException) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}