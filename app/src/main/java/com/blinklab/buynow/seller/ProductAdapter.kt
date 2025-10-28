package com.blinklab.buynow.seller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.blinklab.buynow.R
import com.google.firebase.database.core.Context

class ProductAdapter (val arrayList: ArrayList<ProductModel>):
        RecyclerView.Adapter<ProductAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_design_file,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView= arrayList[position]
        holder.namme.text=itemView.productName
        holder.colorr.text=itemView.productColor
        holder.price.text=itemView.productPrice
        holder.image.load(itemView.imageURL)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         val namme = itemView.findViewById<TextView>(R.id.name_of_product_rv)
         val image = itemView.findViewById<ImageView>(R.id.image_of_product_rv)
         val colorr = itemView.findViewById<TextView>(R.id.color_of_product)
         val price = itemView.findViewById<TextView>(R.id.price_of_product)

    }
        }