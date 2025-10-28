package com.blinklab.buynow.buyer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blinklab.buynow.R
import com.blinklab.buynow.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
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

        binding.createNewAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEdittext.text.toString().trim()
            val password = binding.passwordEdittext.text.toString().trim()
            when{
                email.isEmpty()-> showToast("Enter email")
                password.isEmpty()-> showToast("Enter password")
                else->{
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                        when{
                            task.isSuccessful->{
                                showToast("Login Successful")
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            }
                            else-> showToast(task.exception?.message.toString())
                        }
                    }
                }
            }
        }
    }
    private fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    /*override fun onResume() {
        super.onResume()
        if (auth.currentUser?.uid!=null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }*/
}