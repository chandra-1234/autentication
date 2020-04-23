package com.chandra.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loginactivity.*


class MainActivity : AppCompatActivity() {
    lateinit var databaseReference: DatabaseReference
    lateinit var ds:DatabaseReference
    var datalist = ArrayList<Data>()
    var auth:FirebaseAuth?=null
    lateinit var currentuser: FirebaseUser

    var authlistener:FirebaseAuth.AuthStateListener?=null
    lateinit var options: FirebaseRecyclerOptions<Data>
    lateinit var adapter: FirebaseRecyclerAdapter<Data, View_Holder>
    var mIth:ItemTouchHelper.SimpleCallback?=null
    var rv: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//       FirebaseDatabase.getInstance().setPersistenceEnabled(true)
      //  currentuser= auth!!.currentUser!!

         auth=FirebaseAuth.getInstance()
      //  databaseReference.keepSynced(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        bindadapter()




    }

    private fun bindadapter() {
        options = FirebaseRecyclerOptions.Builder<Data>()
            .setQuery(databaseReference, Data::class.java).build()

        adapter = object : FirebaseRecyclerAdapter<Data, View_Holder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
                val view: View =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.cardview_layout, parent, false)
                return View_Holder(view)
            }

            override fun onBindViewHolder(p0: View_Holder, p1: Int, p2: Data) {
                p0.title.setText(p2.Title)
                Picasso.get().load(p2.Image).fit().centerCrop().into(p0.imageView)
            }
        }

        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
        recyclerview.adapter = adapter
    }


    private fun searchviewtext(text: String?) {

        val query = text!!.toLowerCase()
        val result: Query =
            databaseReference.orderByChild("Title").startAt(query).endAt(query + "\uf8ff")
        result.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                datalist.clear()
                if (p0.hasChildren()) {
                    for (ds: DataSnapshot in p0.children) {
                        val items: Data? = ds.getValue(Data::class.java)
                        datalist.add(items!!)
                    }
                }
            }

        })


        val adapter = recycyler_adapter(this, datalist)

        adapter.notifyDataSetChanged()
        recyclerview.adapter = adapter


    }


    class View_Holder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var title = itemView.findViewById<TextView>(R.id.displaytitle)
        var imageView = itemView.findViewById<ImageView>(R.id.displayimage)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu?.findItem(R.id.search)?.actionView as androidx.appcompat.widget.SearchView
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchviewtext(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchviewtext(newText)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.additem) {
            val intent = Intent(this, item_add::class.java)
            startActivity(intent)

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser==null)
        {
            Toast.makeText(applicationContext,"login",Toast.LENGTH_LONG).show()
            val intent=Intent(this,loginactivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        else{
            Toast.makeText(applicationContext,"displayingactivity",Toast.LENGTH_LONG).show()
            val intent=Intent(this,displayingactivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        adapter.startListening()

    }

    override fun onDestroy() {
        adapter.stopListening()

        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

        adapter.startListening()

    }



}


