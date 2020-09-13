package com.example.petcare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petcate.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var eemail: EditText
    private lateinit var epass: EditText
    private lateinit var register: TextView
    private lateinit var login: Button
    private lateinit var pbar: ProgressBar
    private lateinit var fa: FirebaseAuth
    private lateinit var fs: FirebaseFirestore
    private var email: String = ""
    private var pass: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        eemail = findViewById(R.id.email)
        epass = findViewById(R.id.password)
        register = findViewById(R.id.register)
        login = findViewById(R.id.login)
        pbar = findViewById(R.id.pbar)
        fa = FirebaseAuth.getInstance()
        fs = FirebaseFirestore.getInstance()

        register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }

        if (fa.currentUser != null) {
            pbar.visibility = View.VISIBLE
            fs.collection("User").document(fa.currentUser!!.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    val email = documentSnapshot.getString("email")
                    if (email != null) {
                        Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity2::class.java))
                        finish()
                    }
                }
        }

        login.setOnClickListener {
            pbar.visibility = View.VISIBLE
            email = eemail.text.toString()
            pass = epass.text.toString()
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(applicationContext, "All Fields Are Required!!", Toast.LENGTH_SHORT).show()
            } else {
                fa.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this@LoginActivity, OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            pbar.visibility = View.GONE
                            fs.collection("User").document(task.result?.user?.uid ?: "")
                                .get()
                                .addOnSuccessListener { documentSnapshot ->
                                    val email = documentSnapshot.getString("email")
                                    if (email != null) {
                                        Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@LoginActivity, MainActivity2::class.java))
                                        finish()
                                    }
                                }
                        } else {
                            pbar.visibility = View.INVISIBLE
                            Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }
}