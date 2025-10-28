package com.blinklab.buynow.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.blinklab.buynow.R
import com.blinklab.buynow.databinding.ActivityMainBinding
import com.blinklab.buynow.databinding.FragmentCartBinding
import com.blinklab.buynow.seller.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase= FirebaseDatabase.getInstance()
    private lateinit var cartAdapter: CartAdapter
    private var cartList= ArrayList<ProductModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding= FragmentCartBinding.inflate(layoutInflater, container, false)
        fetchCategories()
        cartAdapter= CartAdapter(cartList)
        binding.cartRv.adapter=cartAdapter
        return binding.root
    }
    private fun fetchCategories(){
        val uid = auth.currentUser?.uid
        database.reference.child("cart_products").child(uid!!)
            .child("products").addValueEventListener(object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    cartList.clear()
                    for (cartShot in snapshot.children) {
                        val cart = cartShot.getValue(ProductModel::class.java)
                        if (cart != null) {
                            cartList.add(cart)
                            cartAdapter.notifyDataSetChanged()
                        }else{
                            Toast.makeText(context, "No products", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}