package com.blinklab.buynow.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.blinklab.buynow.R
import com.blinklab.buynow.buyer.MenuActivity
import com.blinklab.buynow.buyer.ModelClass
import com.blinklab.buynow.databinding.FragmentProfileBinding
import com.blinklab.buynow.seller.ProductModel
import com.blinklab.buynow.seller.SellerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(layoutInflater, container, false)
        fetchDetails()
        binding.logoutProfileBtn.setOnClickListener {
            startActivity(Intent(context, MenuActivity::class.java))
        }
        return binding.root

    }
    private fun fetchDetails(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().reference.child("user").child(uid!!).child("Profile")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(ModelClass::class.java)
                    binding.nameProfile.text="Name: ${user?.name}"
                    binding.ageProfile.text="Age:${user?.age}"
                    binding.genderProfile.text="Gender:${user?.gender}"
                    binding.emailProfile.text="Email:${user?.email}"
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "no data", Toast.LENGTH_SHORT).show()
                }
            })
    }
}