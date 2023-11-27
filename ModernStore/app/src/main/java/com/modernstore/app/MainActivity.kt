package com.modernstore.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.modernstore.app.ui.account.AccountFragment
import com.modernstore.app.ui.cart.CartFragment
import com.modernstore.app.ui.login.LoginActivity
import com.modernstore.app.ui.register.RegisterActivity
import com.modernstore.app.ui.shopping.ShoppingFragment

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(ShoppingFragment())
        bottomNav = findViewById(R.id.bottomnav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(ShoppingFragment())
                    true
                }
                R.id.message -> {
                    loadFragment(CartFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(AccountFragment())
                    true
                }
                else -> throw IllegalArgumentException("Unhandled itemId: ${it.itemId}")
            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

}