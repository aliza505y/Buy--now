package com.blinklab.buynow.seller

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blinklab.buynow.R
import com.blinklab.buynow.databinding.ActivityAddCategoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class AddCategoryActivity : AppCompatActivity() {
    private val binding: ActivityAddCategoryBinding by lazy{
        ActivityAddCategoryBinding.inflate(layoutInflater)
    }
    private  var categoryList= ArrayList<CategoryModel>()
    private lateinit var categoryAdapter: CategoryAdapter
    private var imageURI: Uri?= null
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
        binding.imageViewCategory.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        fetchCategories()

        categoryAdapter = CategoryAdapter(categoryList, this)
        binding.categoryRv.adapter=categoryAdapter


        binding.uploadCategoryBtn.setOnClickListener {
            val name = binding.nameEditableCategory.text.toString().trim()
            if (imageURI == null){
                Toast.makeText(this, "Enter image of category", Toast.LENGTH_SHORT).show()
            }else if (name.isEmpty()){
                Toast.makeText(this, "Enter name of category", Toast.LENGTH_SHORT).show()
            } else{
                uploadCategory(imageURI!!,name)
            }
        }
    }


    private fun uploadCategory(imageURI: Uri, categoryName: String){
        binding.progressBarCategory.visibility= View.VISIBLE
        binding.uploadCategoryBtn.visibility= View.INVISIBLE
        val uid =auth.currentUser?.uid
        //upload image
        val fileRef= storage.reference.child("category_images").child(uid!!).child("${System.currentTimeMillis()}.png")
        fileRef.putFile(imageURI).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { url ->
                //upload realtime database
                val category = CategoryModel(categoryName, url.toString())
                database.reference.child("user").child(uid.toString()).child("categories")
                    .child(categoryName).setValue(category).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            binding.progressBarCategory.visibility = View.INVISIBLE
                            binding.uploadCategoryBtn.visibility = View.VISIBLE
                            Toast.makeText(
                                this,
                                "Category uploaded successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            binding.imageViewCategory.setImageResource(R.drawable.ic_launcher_background)
                            binding.nameEditableCategory.text.clear()
                        } else {
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }


    private fun fetchCategories(){
        val uid = auth.currentUser!!.uid
        val categoryName =binding.nameEditableCategory.text.toString().trim()
        database.reference.child("user").child(uid).child("categories").addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryList .clear()
               for (categorySnap in snapshot.children){
                   val  categories =categorySnap.getValue(CategoryModel::class.java)
                   if (categories!=null){
                       categoryList.add(categories)
                       categoryAdapter.notifyDataSetChanged()
                   }else{
                       showToast("No categories found")
                   }

               }

            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.message)
            }
        })

    }

    private fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageURI = it
        try {
            binding.imageViewCategory.setImageURI(imageURI)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}