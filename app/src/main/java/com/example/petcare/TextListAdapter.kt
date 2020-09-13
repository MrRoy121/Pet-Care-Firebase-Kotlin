package com.example.petcare

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petcate.R

class TextListAdapter(private val context: Context, private val list: List<String>, private val uid: List<String>) :
    RecyclerView.Adapter<TextListAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = inflater.inflate(R.layout.text_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.serialNumber.text = list[position]
        holder.lout.setOnClickListener {
            val intent = Intent(context, DoctorMessage::class.java)
            intent.putExtra("uid", uid[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serialNumber: TextView = itemView.findViewById(R.id.itemTextView)
        val lout: LinearLayout = itemView.findViewById(R.id.lout)
    }
}