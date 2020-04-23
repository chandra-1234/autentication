package com.chandra.firebaseapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.loginactivity.*
import kotlinx.android.synthetic.main.signupactivity.*

class signupactivity:AppCompatActivity() {

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signupactivity)
        database = FirebaseDatabase.getInstance().getReference("LoginData")

        loginbtn.setOnClickListener {
            login()
        }

    }
    private fun login() {
        val country = code2.text.toString()
        val phone = number2.text.toString()
        if ((country + phone).length < 10 || (country + phone).isEmpty()) {
            number2.setError("Enter a valid mobile");
            number2.requestFocus();
            return;
        }
        else {
            addfirebase(country, phone)
            val number = "+" + country + phone
            val intent = Intent(this, verificationactivity::class.java)
            intent.putExtra("mobile", number)
            startActivity(intent)
        }
    }
    private fun addfirebase(country:String,code:String){
        database.child("Number").setValue("+"+country+code)
    }

}