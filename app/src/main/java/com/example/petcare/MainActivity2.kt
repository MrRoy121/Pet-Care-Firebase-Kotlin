package com.example.petcare

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcate.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class MainActivity2 : AppCompatActivity() {

    private lateinit var adapter: TextListAdapter
    private val rray: MutableList<String> = ArrayList()
    private val uid: MutableList<String> = ArrayList()
    private lateinit var fs: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        fs = FirebaseFirestore.getInstance()

        findViewById<View>(R.id.signout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity2, LoginActivity::class.java))
            finish()
        }

        val listView: RecyclerView = findViewById(R.id.list)
        listView.setHasFixedSize(true)
        listView.layoutManager = LinearLayoutManager(this)

        fs.collection("Message").get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    for (documentSnapshot in queryDocumentSnapshots) {
                        fs.collection("User").document(documentSnapshot.id).get()
                            .addOnSuccessListener { d ->
                                if (d.exists()) {
                                    uid.add(d.id)
                                    rray.add(d.getString("name") ?: "")
                                }
                                adapter = TextListAdapter(this@MainActivity2, rray, uid)
                                listView.adapter = adapter
                            }
                    }
                }
            }
    }
}