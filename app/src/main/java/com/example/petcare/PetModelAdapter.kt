package com.example.petcare

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.petcate.R

class PetModelAdapter(
    private val courseDataArrayList: ArrayList<Pet_Model>,
    private val mcontext: Context
) : RecyclerView.Adapter<PetModelAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pet_item, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val recyclerData = courseDataArrayList[position]

        when (recyclerData.type) {
            "Dog" -> {
                holder.img.setBackgroundResource(R.drawable.dog)
                holder.color.setBackgroundResource(R.color.dog)
            }
            "Cat" -> {
                holder.img.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.cat))
                holder.color.setBackgroundResource(R.color.cat)
            }
            "Parrot" -> {
                holder.img.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.parrot))
                holder.color.setBackgroundResource(R.color.parrot)
            }
        }
        holder.name.text = recyclerData.name

        holder.card.setOnClickListener {
            Toast.makeText(mcontext, recyclerData.breed, Toast.LENGTH_SHORT).show()
            val i = Intent(mcontext, DietPlan::class.java).apply {
                putExtra("Name", recyclerData.name)
                putExtra("Breed", recyclerData.breed)
                putExtra("Types", recyclerData.type)
                putExtra("Age", recyclerData.age)
                putExtra("uid", recyclerData.uid)
            }
            mcontext.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return courseDataArrayList.size
    }

    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val img: ImageView = itemView.findViewById(R.id.img)
        val color: ImageView = itemView.findViewById(R.id.color)
        val card: CardView = itemView.findViewById(R.id.card)
    }
}