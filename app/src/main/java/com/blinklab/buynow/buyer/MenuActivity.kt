package com.blinklab.buynow.buyer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blinklab.buynow.R
import com.blinklab.buynow.databinding.ActivityMenuBinding
import com.blinklab.buynow.seller.SellerActivity
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val binding: ActivityMenuBinding by lazy {
        ActivityMenuBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth= FirebaseAuth.getInstance()
        binding.backBtnMenu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.switchToSeller.setOnClickListener {
            startActivity(Intent(this@MenuActivity, SellerActivity::class.java))
        }
        binding.logout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MenuActivity, LoginActivity::class.java))
        }
    }
}