package com.example.petcare

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.petcate.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DietPlan : AppCompatActivity() {

    private lateinit var name: TextView
    private lateinit var breed: TextView
    private lateinit var age: TextView
    private lateinit var con: CardView
    private lateinit var fs: FirebaseFirestore
    private lateinit var webView: WebView
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_plan)

        name = findViewById(R.id.name)
        breed = findViewById(R.id.breed)
        age = findViewById(R.id.age)
        con = findViewById(R.id.con)
        fs = FirebaseFirestore.getInstance()
        webView = findViewById(R.id.videoView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        intent?.let {
            it.getStringExtra("Breed")?.let { breed ->
                name.text = it.getStringExtra("Name")
                age.text = it.getStringExtra("Age")
                this.breed.text = breed
                uid = it.getStringExtra("uid") ?: ""

                val videoUrl = when (breed) {
                    "Bulldog" -> "https://youtu.be/-0zMXu7JCaQ"
                    "German Shepherd" -> "https://youtu.be/Fpuq9ZOElkY"
                    "Labrador Retriever" -> "https://youtu.be/GMfSKucTZEQ"
                    "Golden Retriever" -> "https://youtu.be/l4YkALX-dSk"
                    "Siberian Husky" -> "https://youtu.be/lfEYGtrbNVE"
                    "Dobermann" -> "https://youtu.be/OsTX2DYEBD4"
                    "Persian Cat" -> "https://youtu.be/bME7PxUNHFk"
                    "British Shorthair" -> "https://youtu.be/43E-0fVdalI"
                    "Bengal Cat" -> "https://youtu.be/QLYxSLx1CZ4"
                    "Ragdoll" -> "https://youtu.be/CRd2IplnYEE"
                    "Scottish Fold" -> "https://youtu.be/EPc-twdqb6g"
                    "Siberian Cat" -> "https://youtu.be/BI1leWxuAlw"
                    "Conures" -> "https://youtu.be/VNaKAPW5BWg"
                    "Cockatoos" -> "https://youtu.be/p4sfoD1EyaY"
                    "Cockatiels" -> "https://youtu.be/jgrjIQDlKgc"
                    "Caiques" -> "https://youtu.be/HCIFuHLfurw"
                    "Lovebirds" -> "https://youtu.be/HCIFuHLfurw"
                    "Electus" -> "https://youtu.be/xZ5cLXQtUfQ"
                    else -> "https://youtu.be/w_VSfN5Hf6Q"
                }
                webView.loadUrl(videoUrl)
                webView.webViewClient = LoadWebView()
            }
        }

        con.setOnClickListener {
            startActivity(Intent(this, ContactDoctor::class.java))
        }

        findViewById<CardView>(R.id.delete).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Pet")
                .setMessage("Do you really want to Delete Current pet?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    fs.collection("Pets").document(uid).delete()
                    Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .setNegativeButton(android.R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private class LoadWebView : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}