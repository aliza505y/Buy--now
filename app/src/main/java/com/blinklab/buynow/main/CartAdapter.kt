package com.blinklab.buynow.main

import android.content.Context
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(val cartArray: ArrayList<ProductModel>):
        RecyclerView.Adapter<CartAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val img = itemView.findViewById<ImageView>(R.id.image_of_product_cart)
        val namee = itemView.findViewById<TextView>(R.id.name_of_product_cart)
        val colorr = itemView.findViewById<TextView>(R.id.color_of_product_cart)
        val price = itemView.findViewById<TextView>(R.id.price_of_product_cart)
        val delete = itemView.findViewById<ImageView>(R.id.delete_product)
    }
    val uid = FirebaseAuth.getInstance().currentUser?.uid


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_design_file,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val item = cartArray[position]
        holder.img.load(item.imageURL)
        holder.namee.text=item.productName
        holder.colorr.text=item.productColor
        holder.price.text=item.productPrice
        holder.delete.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("cart_products").child(uid!!)
                .child("products").child(item.productName).removeValue().addOnCompleteListener {
                    if (it.isSuccessful){
                        val context = holder.itemView.context
                        Toast.makeText(context, "delete product", Toast.LENGTH_SHORT).show()
                    }else{
                        val context = holder.itemView.context
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun getItemCount(): Int {
        return cartArray.size
    }
}