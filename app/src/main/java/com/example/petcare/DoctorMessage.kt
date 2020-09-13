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
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.MutableMap

class DoctorMessage : AppCompatActivity() {

    private lateinit var attach: ImageView
    private lateinit var send: ImageView
    private lateinit var img: ImageView
    private lateinit var dlt: ImageView
    private lateinit var imglout: CardView
    private lateinit var editText: EditText
    private lateinit var rv1: RecyclerView
    private lateinit var fa: FirebaseAuth
    private lateinit var fs: FirebaseFirestore
    private lateinit var pAdapter: messageDocAdapter
    private var messageList: MutableList<messageModel> = ArrayList()
    private lateinit var params: MutableMap<String, String>
    private lateinit var empty: MutableMap<String, String>
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_message)

        attach = findViewById(R.id.attach)
        send = findViewById(R.id.send)
        editText = findViewById(R.id.edittext)
        imglout = findViewById(R.id.imglout)
        img = findViewById(R.id.img)
        dlt = findViewById(R.id.dlt)
        rv1 = findViewById(R.id.rv1)
        fa = FirebaseAuth.getInstance()
        fs = FirebaseFirestore.getInstance()

        messageList = ArrayList()
        rv1.setHasFixedSize(true)
        rv1.layoutManager = LinearLayoutManager(this)
        val i = intent
        if (i.getStringExtra("uid") != null) {
            uid = i.getStringExtra("uid")!!
            fs.collection("Message").document(i.getStringExtra("uid")!!).collection("DateTime")
                .orderBy("dtime", Query.Direction.ASCENDING).get().addOnSuccessListener { queryDocumentSnapshots ->
                    if (!queryDocumentSnapshots.isEmpty) {
                        for (d in queryDocumentSnapshots) {
                            val bod = d.getString("status") == "1"
                            messageList.add(messageModel(d.getString("message"), d.getString("dtime"), bod))
                        }
                        pAdapter = messageDocAdapter(this@DoctorMessage, messageList)
                        rv1.adapter = pAdapter
                    }
                }
        }

        findViewById<View>(R.id.refresh).setOnClickListener {
            finish()
            startActivity(intent)
        }
        findViewById<View>(R.id.back).setOnClickListener { finish() }

        send.setOnClickListener {
            val message = editText.text.toString()
            if (message.isNotEmpty()) {
                val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date: String = df.format(Calendar.getInstance().time)

                empty = HashMap()
                empty["Null"] = "NULL"
                params = HashMap()
                params["message"] = message
                params["status"] = "0"
                params["dtime"] = date
                val ds: DocumentReference = fs.collection("Message").document(uid)
                ds.set(empty)
                ds.collection("DateTime").add(params)
                finish()
                startActivity(intent)
            }
        }
    }
}