package com.blinklab.buynow.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.blinklab.buynow.R
import com.blinklab.buynow.seller.ProductModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.Context

class ProductMainAdapter (val arrayList: ArrayList<ProductModel>):
        RecyclerView.Adapter<ProductMainAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val img = itemView.findViewById<ImageView>(R.id.product_main_image)
        val nam = itemView.findViewById<TextView>(R.id.product_name_main)
        val colour = itemView.findViewById<TextView>(R.id.product_color_main)
        val price = itemView.findViewById<TextView>(R.id.product_price_main)
        val addtocart = itemView.findViewById<MaterialButton>(R.id.add_to_cart_btn)

    }
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductMainAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_main_rv_design,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductMainAdapter.ViewHolder, position: Int) {
        val itemView = arrayList[position]
        holder.img.load(itemView.imageURL)
        holder.nam.text=itemView.productName
        holder.colour.text=itemView.productColor
        holder.price.text=itemView.productPrice

        holder.addtocart.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("cart_products").child(uid!!)
                .child("products").child(System.currentTimeMillis().toString()).setValue(itemView)
                .addOnCompleteListener { 
                    if (it.isSuccessful){
                        val context = holder.itemView.context
                        Toast.makeText(context, "added", Toast.LENGTH_SHORT).show()
                    }else{
                        val context = holder.itemView.context
                        Toast.makeText(context, "error in adding", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}