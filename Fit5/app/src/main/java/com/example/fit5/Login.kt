package com.example.fit5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    //variables
    private lateinit var toRegisterBtn: Button
    lateinit var btnLogin: Button
    lateinit var edEmailLogin:EditText
    lateinit var  edPasswordLogin: EditText
    //firebase
    private lateinit var auth : FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //type casting
        toRegisterBtn = findViewById(R.id.RegisterHerebtn)
        btnLogin = findViewById(R.id.btnLogin)
        edEmailLogin = findViewById(R.id.edEmailLogin)
        edPasswordLogin = findViewById(R.id.edPasswordLogin)


        //if user doesnt have an account
        toRegisterBtn.setOnClickListener {
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }

        //initialize firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()



        btnLogin.setOnClickListener {

            val email = edEmailLogin.text.toString().trim()
            val password = edPasswordLogin.text.toString().trim()
                //check fields arent empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email or password cant be empty", Toast.LENGTH_SHORT).show()
                edEmailLogin.requestFocus()
                return@setOnClickListener
            }
            loginUser(email, password)
        }

    }
    //user login
    fun loginUser(email:String, password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {
                    task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Welcome Back", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Login, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "username or password incorrect 💀", Toast.LENGTH_SHORT).show()
                }
                }

        }

}