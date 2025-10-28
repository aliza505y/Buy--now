package com.blinklab.buynow.buyer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blinklab.buynow.R
import com.blinklab.buynow.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.alreadyHaveAnAccount.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }

        binding.signupBtn.setOnClickListener {
            val name = binding.nameEdittext.text.toString().trim()
            val age = binding.ageEdittext.text.toString().trim()
            val gender = binding.genderEdittext.text.toString().trim()
            val email = binding.emailEdittext.text.toString().trim()
            val password = binding.passwordEdittext.text.toString().trim()

            if (name.isEmpty()) {
                showToast("Enter your name")
            } else if (age.isEmpty()) {
                showToast("Enter your age")
            } else if (gender.isEmpty()) {
                showToast("Enter your gender")
            } else if (email.isEmpty()) {
                showToast("Enter your email")
            } else if (password.isEmpty()) {
                showToast("Enter password")
            } else if (password.length<6){
                showToast("Password must be 6 digits")
            }else{
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        val user = ModelClass(name, age, gender, email, password)
                        database.reference.child("user").child(uid!!.toString()).child("Profile").setValue(user).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    showToast("Account created successfully")
                                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                                } else {
                                    showToast(task.exception!!.message.toString())
                                }
                            }
                    }else{
                        showToast(task.exception?.message.toString())
                    }

                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}