package com.modernstore.app.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.modernstore.app.R
import com.modernstore.app.db.preferencemanager.SharedPreferencesHelper
import com.modernstore.app.db.roomdb.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment() {
    private lateinit var sharedpreferences: SharedPreferencesHelper
    private lateinit var appDatabase: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.cart_fragment, container, false)
        sharedpreferences = SharedPreferencesHelper(requireContext())
        appDatabase = AppDatabase.getInstance(requireContext())
        if(sharedpreferences.getLoggedIn()){
            val checkoutButton: Button = rootView.findViewById(R.id.checkoutbutton)
            checkoutButton.visibility = View.VISIBLE
            try {
                lifecycleScope.launch {
                    val getUserId = withContext(Dispatchers.IO) {
                        appDatabase.userDao().getIdByUsername(sharedpreferences.getUserLogged()).toLong()
                    }
                    val cartList = withContext(Dispatchers.IO) {
                        appDatabase.cartDao().getCartsByUser(getUserId)
                    }
                    if (cartList.isEmpty()) {
                        val announce: TextView = rootView.findViewById(R.id.cart_notif)
                        announce.text = getString(R.string.cartempty)
                    }
                    else{
                        val recyclerViewCart: RecyclerView = rootView.findViewById(R.id.recyclerViewCart)
                        val adapter = CartAdapter(cartList)
                        recyclerViewCart.adapter = adapter
                    }
                }
            }
            catch(e: Exception){
                Log.d("Lifecycle Error:", "Error with ${e.message}")
            }
        }
        else{
            val announce: TextView = rootView.findViewById(R.id.cart_notif)
            announce.text = getString(R.string.pleaselogin)
            val checkoutButton: Button = rootView.findViewById(R.id.checkoutbutton)
            checkoutButton.visibility = View.GONE
        }
        return rootView
    }
}