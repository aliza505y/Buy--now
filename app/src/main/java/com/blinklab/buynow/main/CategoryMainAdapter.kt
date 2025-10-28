package com.blinklab.buynow.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.blinklab.buynow.R
import com.blinklab.buynow.seller.CategoryModel

class CategoryMainAdapter (val arrayList: ArrayList<CategoryModel>):
        RecyclerView.Adapter<CategoryMainAdapter.ViewHolder>(){
    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val nname = itemView.findViewById<TextView>(R.id.category_name_main)
        val image = itemView.findViewById<ImageView>(R.id.category_image_main)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMainAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_main_rv_design,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryMainAdapter.ViewHolder, position: Int) {
        val itemView = arrayList[position]
        holder.nname.text=itemView.categoryName
        holder.image.load(itemView.imageURL)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}