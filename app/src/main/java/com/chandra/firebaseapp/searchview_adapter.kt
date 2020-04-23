package com.chandra.firebaseapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class recycyler_adapter(var context: Context, var data:MutableList<Data>): RecyclerView.Adapter<recycyler_adapter.View_Holder>() {


    class View_Holder(itemview: View): RecyclerView.ViewHolder(itemview) {
        var title=itemView.findViewById<TextView>(R.id.displaytitle)
        var img=itemview.findViewById<ImageView>(R.id.displayimage)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_layout,parent,false)
        return View_Holder(view)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: View_Holder, position: Int) {

        var userdata=data[position]
        holder.title.setText(userdata.Title)
        Picasso.get().load(userdata.Image).fit().centerCrop().into(holder.img)

    }

}