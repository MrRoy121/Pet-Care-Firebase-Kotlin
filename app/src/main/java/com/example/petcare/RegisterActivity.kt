package com.example.petcare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petcate.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var ename: EditText
    private lateinit var eemail: EditText
    private lateinit var epass: EditText
    private lateinit var isdoc: Switch
    private lateinit var register: Button
    private lateinit var login: TextView
    private lateinit var pbar: ProgressBar

    private lateinit var fa: FirebaseAuth
    private lateinit var fs: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ename = findViewById(R.id.name)
        eemail = findViewById(R.id.email)
        epass = findViewById(R.id.password)
        isdoc = findViewById(R.id.isdoc)
        register = findViewById(R.id.register)
        login = findViewById(R.id.login)
        pbar = findViewById(R.id.pbar)

        fa = FirebaseAuth.getInstance()
        fs = FirebaseFirestore.getInstance()

        login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        register.setOnClickListener {
            pbar.visibility = View.VISIBLE
            val email = eemail.text.toString()
            val name = ename.text.toString()
            val pass = epass.text.toString()

            val user = hashMapOf(
                "email" to email,
                "name" to name,
                "pass" to pass
            )

            when {
                email.isEmpty() || name.isEmpty() || pass.isEmpty() -> {
                    Toast.makeText(applicationContext, "All Fields Are Required!!", Toast.LENGTH_SHORT).show()
                    pbar.visibility = View.INVISIBLE
                }
                pass.length < 6 -> {
                    Toast.makeText(applicationContext, "Password Must Be 6 Characters!!", Toast.LENGTH_SHORT).show()
                    pbar.visibility = View.INVISIBLE
                }
                else -> {
                    fa.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this@RegisterActivity) { task ->
                            if (task.isSuccessful) {
                                val userId = fa.currentUser?.uid
                                if (isdoc.isChecked) {
                                    fs.collection("Doctor").document(userId!!)
                                        .set(user)
                                        .addOnSuccessListener {
                                            pbar.visibility = View.GONE
                                            Toast.makeText(applicationContext, "Register Successful", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(applicationContext, "Error: $e", Toast.LENGTH_SHORT).show()
                                            pbar.visibility = View.INVISIBLE
                                        }
                                } else {
                                    fs.collection("User").document(userId!!)
                                        .set(user)
                                        .addOnSuccessListener {
                                            pbar.visibility = View.GONE
                                            Toast.makeText(applicationContext, "Register Successful", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(applicationContext, "Error: $e", Toast.LENGTH_SHORT).show()
                                            pbar.visibility = View.INVISIBLE
                                        }
                                }
                            } else {
                                Toast.makeText(applicationContext, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                pbar.visibility = View.INVISIBLE
                            }
                        }
                }
            }
        }
    }
}