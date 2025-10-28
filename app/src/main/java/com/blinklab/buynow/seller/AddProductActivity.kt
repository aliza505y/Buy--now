package com.blinklab.buynow.seller

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blinklab.buynow.R
import com.blinklab.buynow.databinding.ActivityAddCategoryBinding
import com.blinklab.buynow.databinding.ActivityAddProductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.time.Clock.system
import kotlin.time.Clock

class AddProductActivity : AppCompatActivity() {
    private val binding: ActivityAddProductBinding by lazy{
        ActivityAddProductBinding.inflate(layoutInflater)
    }
    private var productList = ArrayList<ProductModel>()
    private lateinit var productAdapter : ProductAdapter
    private var imageURI: Uri?=null
    private  var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private  var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var storage: FirebaseStorage= FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fetchProduct()
        productAdapter= ProductAdapter(  productList)
        binding.productRv.adapter=productAdapter

        binding.categoryName.text = intent.getStringExtra("name")
        binding.productImageEdit.setOnClickListener {
            gallery.launch("image/*")
        }

        binding.uploadProductBtn.setOnClickListener {
            val name = binding.productNameEdit.text.toString().trim()
            val color = binding.productColorEdit.text.toString().trim()
            val price = binding.productPriceEdit.text.toString().trim()
            if (imageURI == null) {
                Toast.makeText(this, "Select product image", Toast.LENGTH_SHORT).show()
            } else if (name.isEmpty()) {
                Toast.makeText(this, "Enter name of product", Toast.LENGTH_SHORT).show()
            }else if (color.isEmpty()) {
                Toast.makeText(this, "Enter color of product", Toast.LENGTH_SHORT).show()
            }else if (price.isEmpty()) {
                Toast.makeText(this, "Enter size of product", Toast.LENGTH_SHORT).show()
            } else {
                uploadProduct(imageURI!!, name ,color,price)
            }
        }

    }

    private fun fetchProduct(){
        val uid = auth.currentUser?.uid
        database.reference.child("All_products")
            .addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (categorySnap in snapshot.children){
                    val products = categorySnap.getValue(ProductModel::class.java)
                    if (products!=null){
                        productList.add(products)
                        productAdapter.notifyDataSetChanged()
                    }else{
                        showText("No Products")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showText(error.message)
            }
        })

    }

    private fun uploadProduct(imageUri: Uri ,productName: String ,productColor: String,productSize: String){
        binding.progressBarProduct.visibility= View.VISIBLE
        binding.uploadProductBtn.visibility= View.INVISIBLE
        val uid = auth.currentUser?.uid
        val fileRef = storage.reference.child("product_images").child(uid!!).child("${System.currentTimeMillis()}.png")
        fileRef.putFile(imageUri!!).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                val name = binding.productNameEdit.text.toString().trim()
                val category = binding.categoryName.text.toString()
                val product = ProductModel(it.toString(),productName,productColor,productSize)
                database.reference.child("user").child(uid).child("categories").child(binding.categoryName.text.toString()).child("products")
                    .child("${System.currentTimeMillis()}").setValue(product).addOnCompleteListener { task->
                        if (task.isSuccessful){
                            database.reference.child("All_products").child("$name"+ "-"+ "$category" + "-" +"${System.currentTimeMillis()}").setValue(product).addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(this, "Product upload", Toast.LENGTH_SHORT).show()
                                    binding.progressBarProduct.visibility= View.INVISIBLE
                                    binding.uploadProductBtn.visibility= View.VISIBLE
                                    binding.productNameEdit.text.clear()
                                    binding.productColorEdit.text.clear()
                                    binding.productPriceEdit.text.clear()
                                    binding.productImageEdit.setImageResource(R.drawable.ic_launcher_background)
                                }else{
                                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else{
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
    private val gallery = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageURI = it
        try {
            binding.productImageEdit.setImageURI(imageURI)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun showText(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}



