package com.example.petcare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.petcate.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Addnewpet : AppCompatActivity() {

    private var types: String? = null
    private lateinit var dogbreeds: List<String>
    private lateinit var catbreeds: List<String>
    private lateinit var parrotbreeds: List<String>
    private lateinit var dataAdapter: ArrayAdapter<String>
    private lateinit var fs: FirebaseFirestore
    private lateinit var fa: FirebaseAuth
    private lateinit var breed: TextView
    private lateinit var item: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnewpet)

        val spinner = findViewById<Spinner>(R.id.spbreed)
        val prog = findViewById<ProgressBar>(R.id.prog)
        val petname = findViewById<EditText>(R.id.pname)
        breed = findViewById(R.id.breed)
        val petage = findViewById<EditText>(R.id.age)
        fs = FirebaseFirestore.getInstance()
        fa = FirebaseAuth.getInstance()

        dogbreeds = listOf(
            "Bulldog",
            "German Shepherd",
            "Labrador Retriever",
            "Golden Retriever",
            "Siberian Husky",
            "Dobermann"
        )

        catbreeds = listOf(
            "Persian Cat",
            "British Shorthair",
            "Bengal Cat",
            "Ragdoll",
            "Scottish Fold",
            "Siberian Cat"
        )

        parrotbreeds = listOf(
            "Conures",
            "Cockatoos",
            "Lovebirds",
            "Cockatiels",
            "Electus",
            "Caiques"
        )

        val i = intent
        types = i.getStringExtra("types")
        when (types) {
            "Dog" -> dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dogbreeds)
            "Cat" -> dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, catbreeds)
            "Parrot" -> dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, parrotbreeds)
            else -> dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf())
        }

        spinner.adapter = dataAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                item = adapterView?.getItemAtPosition(i).toString()
                breed.text = item
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                item = ""
            }
        }

        val addnewpet = findViewById<Button>(R.id.addnewpet)
        addnewpet.setOnClickListener {
            prog.visibility = View.VISIBLE
            val name = petname.text.toString()
            val age = petage.text.toString()
            if (name.isEmpty() || age.isEmpty() || item.isEmpty()) {
                Toast.makeText(this@Addnewpet, "All Fields Are Required!!", Toast.LENGTH_SHORT).show()
            } else {
                val details = hashMapOf(
                    "Pet Name" to name,
                    "Pet Age" to age,
                    "Type" to types,
                    "Breed" to item,
                    "User" to fa.currentUser!!.uid
                )
                fs.collection("Pets").add(details).addOnSuccessListener(OnSuccessListener { documentReference ->
                    Toast.makeText(this@Addnewpet, "Pet Added", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Addnewpet, MainActivity::class.java))
                    finish()
                })
            }
        }
    }
}