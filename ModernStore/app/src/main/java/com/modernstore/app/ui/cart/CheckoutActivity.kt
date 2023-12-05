package com.modernstore.app.ui.cart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.modernstore.app.MainActivity
import com.modernstore.app.R

class CheckoutActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkout_complete)
        val back: Button = findViewById(R.id.backtomain)
        back.setOnClickListener {
            val i = Intent(this@CheckoutActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}