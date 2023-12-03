package com.modernstore.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.modernstore.app.db.roomdb.AppDatabase
import com.modernstore.app.ui.account.AccountFragment
import com.modernstore.app.ui.cart.CartFragment
import com.modernstore.app.ui.shopping.ShoppingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav : BottomNavigationView
    private lateinit var appDatabase: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appDatabase = AppDatabase.getInstance(this)
        loadFragment(ShoppingFragment())
        bottomNav = findViewById(R.id.bottomnav)
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