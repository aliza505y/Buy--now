package com.blinklab.buynow.seller

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blinklab.buynow.R
import com.blinklab.buynow.buyer.MainActivity
import com.blinklab.buynow.databinding.ActivitySellerBinding

class SellerActivity : AppCompatActivity() {
    private val binding: ActivitySellerBinding by lazy {
        ActivitySellerBinding.inflate(layoutInflater)
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
        binding.addNewCategoryBtn.setOnClickListener {
            startActivity(Intent(this@SellerActivity, AddCategoryActivity::class.java))
        }
        binding.switchToBuyer.setOnClickListener {
            startActivity(Intent(this@SellerActivity, MainActivity::class.java  ))
        }
    }
}