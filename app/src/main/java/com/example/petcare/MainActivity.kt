package com.example.petcare

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerDataArrayList: ArrayList<Pet_Model>
    private lateinit var nopet: TextView
    private lateinit var addpet: CardView
    private lateinit var fs: FirebaseFirestore
    private lateinit var fa: FirebaseAuth
    private lateinit var logout: ImageView
    private var types: String? = null
    private var adapter: Pet_Model_Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nopet = findViewById(R.id.nopet)
        recyclerView = findViewById(R.id.rvpet)
        logout = findViewById(R.id.logout)
        addpet = findViewById(R.id.addpet)
        fs = FirebaseFirestore.getInstance()
        fa = FirebaseAuth.getInstance()

        recyclerDataArrayList = ArrayList()
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager

        fs.collection("Pets").whereEqualTo("User", fa.currentUser?.uid).get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    for (d in queryDocumentSnapshots) {
                        Log.e("sas", d.getString("Pet Name") ?: "")
                        recyclerDataArrayList.add(
                            Pet_Model(
                                d.getString("Pet Name") ?: "",
                                d.getString("Breed") ?: "",
                                d.getString("Type") ?: "",
                                d.getString("Pet Age") ?: "",
                                d.id
                            )
                        )
                    }
                    adapter = Pet_Model_Adapter(recyclerDataArrayList, this@MainActivity)
                    recyclerView.adapter = adapter
                    if (recyclerDataArrayList.isNotEmpty()) {
                        nopet.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        nopet.visibility = View.VISIBLE
                        recyclerView.visibility = View.INVISIBLE
                    }
                } else {
                    nopet.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                }
            }

        logout.setOnClickListener {
            fa.signOut()
            finish()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        addpet.setOnClickListener { addNewPet() }
    }

    private fun addNewPet() {
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.addnewpet)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        val dog: CardView = dialog.findViewById(R.id.dog)
        val cat: CardView = dialog.findViewById(R.id.cat)
        val parrot: CardView = dialog.findViewById(R.id.parrot)

        dog.setOnClickListener {
            types = "Dog"
            dialog.dismiss()
            finish()
            val i = Intent(this@MainActivity, Addnewpet::class.java)
            i.putExtra("types", types)
            startActivity(i)
        }

        cat.setOnClickListener {
            types = "Cat"
            dialog.dismiss()
            finish()
            val i = Intent(this@MainActivity, Addnewpet::class.java)
            i.putExtra("types", types)
            startActivity(i)
        }

        parrot.setOnClickListener {
            types = "Parrot"
            dialog.dismiss()
            finish()
            val i = Intent(this@MainActivity, Addnewpet::class.java)
            i.putExtra("types", types)
            startActivity(i)
        }

        dialog.show()
    }
}