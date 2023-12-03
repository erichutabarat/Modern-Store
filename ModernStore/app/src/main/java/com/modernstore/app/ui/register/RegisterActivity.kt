package com.modernstore.app.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.modernstore.app.R
import com.modernstore.app.db.roomdb.AppDatabase
import com.modernstore.app.db.roomdb.User
import com.modernstore.app.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var appDatabase: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)
        appDatabase = AppDatabase.getInstance(this)
        val toLogin : TextView = findViewById(R.id.from_register_to_login)
        toLogin.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
        val registerButton: Button = findViewById(R.id.registerbutton)
        registerButton.setOnClickListener {
            val getEmail: EditText = findViewById(R.id.register_email)
            val getUser: EditText = findViewById(R.id.register_username)
            val getPass: EditText = findViewById(R.id.register_password)

            if (getEmail.text.toString().trim().isNotEmpty() &&
                getUser.text.toString().trim().isNotEmpty() &&
                getPass.text.toString().trim().isNotEmpty()
            ) {
                val user = User(
                    username = getUser.text.toString(),
                    password = getPass.text.toString(),
                    email = getEmail.text.toString()
                )
                insertUser(user)
            } else {
                Toast.makeText(this@RegisterActivity, "Please enter your account", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun insertUser(user: User) {
        // Use a CoroutineScope to run the database operation on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appDatabase.userDao().insertUser(user)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "User registered successfully", Toast.LENGTH_SHORT).show()
                }
                val i = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            } catch (e: Exception) {
                // Handle the exception, e.g., show an error Toast
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}