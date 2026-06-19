package com.example.womensafetyapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.womensafetyapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar)
        setContentView(R.layout.activity_login)
        
        auth = FirebaseAuth.getInstance()
        
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if (btnLogin.text.toString().contains("Login", ignoreCase = true)) {
                    // Try Login
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    // Try Sign Up
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener {
            if (btnLogin.text.toString().contains("Login", ignoreCase = true)) {
                btnLogin.text = "SIGN UP"
                tvRegister.text = "Already have an account? Login"
            } else {
                btnLogin.text = "LOGIN"
                tvRegister.text = "Don't have an account? Sign Up"
            }
        }
    }
}
