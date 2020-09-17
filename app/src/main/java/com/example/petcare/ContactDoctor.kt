package com.example.petcare

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcate.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import messageDocAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set

class ContactDoctor : AppCompatActivity() {

    private lateinit var attach: ImageView
    private lateinit var send: ImageView
    private lateinit var editText: EditText
    private lateinit var imglout: CardView
    private lateinit var refresh: CardView
    private lateinit var back: CardView
    private lateinit var img: ImageView
    private lateinit var dlt: ImageView
    private lateinit var rv1: RecyclerView
    private lateinit var pAdapter: messageDocAdapter
    private var messageList: MutableList<messageModel> = mutableListOf()
    private lateinit var fa: FirebaseAuth
    private lateinit var fs: FirebaseFirestore
    private lateinit var params: MutableMap<String, String>
    private lateinit var empty: MutableMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_doctor)

        attach = findViewById(R.id.attach)
        send = findViewById(R.id.send)
        editText = findViewById(R.id.edittext)
        imglout = findViewById(R.id.imglout)
        img = findViewById(R.id.img)
        dlt = findViewById(R.id.dlt)
        rv1 = findViewById(R.id.rv1)
        refresh = findViewById(R.id.refresh)
        back = findViewById(R.id.back)
        fa = FirebaseAuth.getInstance()
        fs = FirebaseFirestore.getInstance()

        rv1.setHasFixedSize(true)
        rv1.layoutManager = LinearLayoutManager(this)

        fs.collection("Message")
            .document(fa.currentUser!!.uid)
            .collection("DateTime")
            .orderBy("dtime", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    messageList.clear()
                    for (d in queryDocumentSnapshots) {
                        val bod = d.getString("status") == "0"
                        messageList.add(messageModel(d.getString("message")!!, d.getString("dtime")!!, bod))
                    }
                    pAdapter = messageDocAdapter(this@ContactDoctor, messageList)
                    rv1.adapter = pAdapter
                }
            }

        refresh.setOnClickListener {
            finish()
            startActivity(intent)
        }

        back.setOnClickListener {
            finish()
        }

        send.setOnClickListener {
            val message = editText.text.toString()
            if (message.isNotEmpty()) {
                val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date: String = df.format(Calendar.getInstance().time)

                empty = HashMap()
                empty["Null"] = "NULL"
                params = HashMap()
                params["message"] = message
                params["status"] = "1"
                params["dtime"] = date

                val ds: DocumentReference = fs.collection("Message").document(fa.currentUser!!.uid)
                ds.set(empty)
                ds.collection("DateTime").add(params)
                    .addOnSuccessListener {
                        finish()
                        startActivity(intent)
                    }
            }
        }
    }
}