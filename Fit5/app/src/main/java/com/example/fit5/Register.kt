package com.example.fit5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    //variables
     private lateinit var toLoginBtn: Button
    private lateinit var edEmail: EditText
    private  lateinit var edPassword: EditText
    private   lateinit var edConfirmPassword: EditText
    private  lateinit var  mAuth: FirebaseAuth
    private lateinit var Registerbtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //typecasting
        toLoginBtn = findViewById(R.id.LoginHerebtn)
        edEmail = findViewById(R.id.edEmailRegister)
        edPassword = findViewById(R.id.edPasswordRegister)
        edConfirmPassword = findViewById(R.id.edPasswordRegister)
        Registerbtn = findViewById(R.id.Registerbtn)
        mAuth = FirebaseAuth.getInstance()

        //if user already has an account
        toLoginBtn.setOnClickListener {
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }


        Registerbtn.setOnClickListener {
            registerUser(edEmail.text.toString(), edPassword.text.toString())
            val intent = Intent(this@Register, MainActivity::class.java)
            startActivity(intent)
        }





    }//on create ends

    //check user reg details
    fun validateForm(): Boolean {
        val email = edEmail.text.toString()
        val password = edPassword.text.toString()
        val confirmPassword = edConfirmPassword.text.toString()

        when {
            //ensure email field is filled in
            email.isEmpty() -> {
                edEmail.error = "Email is required"
                return false
            }
            //ensures password field is filled
            password.isEmpty() -> {
                edPassword.error = "Password is required"
                return false
            }
            //check password is at least characters long
            password.length < 6 -> {
                edPassword.error = "Password must be at least 6 characters long"
                return false
            }
            //checks password has 1 digit
            !password.any { it.isDigit() } -> {
                edPassword.error = "Password must contain at least one digit"
                return false
            }
            //check password has upper case character
            !password.any { it.isUpperCase() } -> {
                edPassword.error = "Password must contain at least one uppercase letter"
                return false
            }
            //ensure confirm password field is entered
            confirmPassword.isEmpty() -> {
                edConfirmPassword.error = "Confirm password is required"
                return false
            }
            //check if password and confirm password match
            password != confirmPassword -> {
                edConfirmPassword.error = "Passwords do not match"
                return false
            }
            else -> return true
        }
    }

    // Register the user with Firebase
    fun registerUser(email: String, password: String) {
        if (validateForm()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration success
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    } else {
                        // Registration fail
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }



}
