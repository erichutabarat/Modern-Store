package com.modernstore.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.modernstore.app.MainActivity
import com.modernstore.app.R
import com.modernstore.app.db.preferencemanager.SharedPreferencesHelper
import com.modernstore.app.db.roomdb.AppDatabase
import com.modernstore.app.ui.register.RegisterActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var appDatabase: AppDatabase
    private lateinit var sharedpreferences: SharedPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        appDatabase = AppDatabase.getInstance(this)
        sharedpreferences = SharedPreferencesHelper(this)
        val toRegister: TextView = findViewById(R.id.from_login_to_register)
        toRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        val loginButton: Button = findViewById(R.id.loginbutton)
        loginButton.setOnClickListener {
            val getUsername: EditText = findViewById(R.id.login_username)
            val getPassword: EditText = findViewById(R.id.login_password)

            if (getUsername.text.toString().trim().isNotEmpty() && getPassword.text.toString().trim().isNotEmpty()) {
                val username = getUsername.text.toString().trim()
                val password = getPassword.text.toString().trim()

                // Use a coroutine to perform the database operation asynchronously
                lifecycleScope.launch {
                    val isValidUser = withContext(Dispatchers.IO) {
                        appDatabase.userDao().isValidUser(username, password)
                    }

                    if (isValidUser) {
                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        sharedpreferences.LoggedIn(true)
                        sharedpreferences.UserLogged(username)
                        val i = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@LoginActivity, "Please Input Username Or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
