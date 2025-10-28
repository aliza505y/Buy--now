package com.blinklab.buynow.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blinklab.buynow.R
import com.blinklab.buynow.databinding.ActivityAddCategoryBinding.inflate
import com.blinklab.buynow.databinding.FragmentHomeBinding
import com.blinklab.buynow.seller.CategoryModel
import com.blinklab.buynow.seller.ProductModel
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.internal.notify

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val auth: FirebaseAuth= FirebaseAuth.getInstance()
    private val firebaseDatabase: FirebaseDatabase= FirebaseDatabase.getInstance()
    private lateinit var categoryAdap: CategoryMainAdapter
    private lateinit var productAdap : ProductMainAdapter
    private val categoryArrayList= ArrayList<CategoryModel>()
    private val productList = ArrayList<ProductModel>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding. inflate(layoutInflater ,container, false)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.img))
        imageList.add(SlideModel(R.drawable.img_1))
        imageList.add(SlideModel(R.drawable.img_2))
        imageList.add(SlideModel(R.drawable.img_3))
        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)

        fetchCategory()
        categoryAdap= CategoryMainAdapter(categoryArrayList)
        binding.categoryMainRv.adapter=categoryAdap

        fetchProducts()
        productAdap= ProductMainAdapter(productList)
        binding.productMainRv.adapter=productAdap

        return binding.root

    }
    private fun fetchCategory(){
        val uid =auth.currentUser?.uid
        firebaseDatabase.reference.child("user").child(uid!!).child("categories")
            .addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for (categorySnap in snapshot.children){
                    val category = categorySnap.getValue(CategoryModel::class.java)
                    if (category!=null){
                        categoryArrayList.add(category)
                        categoryAdap.notifyDataSetChanged()
                    }else{
                        showToast("No Category Found")
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                showToast(error.message)
            }
        })
    }

    private fun fetchProducts(){
        val uid = auth.currentUser?.uid
        firebaseDatabase.reference.child("All_products").addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (productShot in snapshot.children){
                    val products = productShot.getValue(ProductModel::class.java)
                    if (products!=null) {
                        productList.add(products)
                        productAdap.notifyDataSetChanged()
                    }else{
                        showToast("No categories")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                error.message
            }
        })
    }

    private fun showToast(msg: String){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}


