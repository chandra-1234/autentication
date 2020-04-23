package com.chandra.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.loginactivity.*


class loginactivity:AppCompatActivity() {
    lateinit var database: DatabaseReference
    var auth:FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginactivity)
        database = FirebaseDatabase.getInstance().getReference("LoginData")

        loginbtn.setOnClickListener {
            login()
        }
        signupactivty.setOnClickListener {
            val intent=Intent(this,signupactivty::class.java)
            startActivity(intent)
        }

    }
    private fun login() {
        val id= FirebaseAuth.getInstance().currentUser?.uid
        database.addListenerForSingleValueEvent(object:ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "error in login", Toast.LENGTH_LONG).show()

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    for (ds: DataSnapshot in p0.children) {

                        if (ds.hasChild(id!!)) {
                            val intent = Intent(this@loginactivity, MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)

                        }
                    }

                }
            }
        })
    }







    }
