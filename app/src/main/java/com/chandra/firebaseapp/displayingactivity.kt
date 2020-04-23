package com.chandra.firebaseapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.displayactivity.*

class displayingactivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.displayactivity)
        mainactivity.setOnClickListener(){
            val inttent=Intent(this,MainActivity::class.java)
            startActivity(inttent)
        }
    }
}