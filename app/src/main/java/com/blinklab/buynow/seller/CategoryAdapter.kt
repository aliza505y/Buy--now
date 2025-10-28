package com.blinklab.buynow.seller

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.blinklab.buynow.R

class CategoryAdapter (val arrayList: ArrayList<CategoryModel>, val context: Context):
        RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.image_of_category_rv)
        val namee = view.findViewById<TextView>(R.id.name_of_category_rv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vieww = LayoutInflater.from(parent.context).inflate(R.layout.category_design_file,parent,false)
        return ViewHolder(vieww)
    }

    override fun getItemCount(): Int {
       return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = arrayList[position]
        holder.namee.text=itemView.categoryName
        holder.image.load(itemView.imageURL)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, AddProductActivity::class.java)
            intent.putExtra("name",itemView.categoryName)
            context.startActivity(intent)
        }
    }
}