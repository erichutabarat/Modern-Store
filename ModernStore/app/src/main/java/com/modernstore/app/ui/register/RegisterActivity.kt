package com.modernstore.app.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.modernstore.app.MainActivity
import com.modernstore.app.R
import com.modernstore.app.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        val toLogin : TextView = findViewById(R.id.from_register_to_login)
        toLogin.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}