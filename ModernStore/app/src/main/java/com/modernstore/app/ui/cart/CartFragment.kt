package com.modernstore.app.ui.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.modernstore.app.R
import com.modernstore.app.db.preferencemanager.SharedPreferencesHelper
import com.modernstore.app.db.roomdb.AppDatabase
import com.modernstore.app.db.roomdb.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment(), CartAdapterListener {
    private lateinit var sharedpreferences: SharedPreferencesHelper
    private lateinit var appDatabase: AppDatabase
    private lateinit var recyclerViewCart: RecyclerView
    private var totalcart: Double = 0.0
    private lateinit var announce: TextView
    private lateinit var checkoutButton: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.cart_fragment, container, false)
        sharedpreferences = SharedPreferencesHelper(requireContext())
        appDatabase = AppDatabase.getInstance(requireContext())
        if(sharedpreferences.getLoggedIn()){
            checkoutButton = rootView.findViewById(R.id.checkoutbutton)
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
                        announce = rootView.findViewById(R.id.cart_notif)
                        announce.text = getString(R.string.cartempty)
                    }
                    else{
                        recyclerViewCart = rootView.findViewById(R.id.recyclerViewCart)
                        val adapter = CartAdapter(cartList.toMutableList(), this@CartFragment)
                        recyclerViewCart.adapter = adapter
                    }
                }
            }
            catch(e: Exception){
                Log.d("Lifecycle Error:", "Error with ${e.message}")
            }
            announce = rootView.findViewById(R.id.cart_notif)
            checkOut(checkoutButton, announce)
        }
        else{
            announce = rootView.findViewById(R.id.cart_notif)
            announce.text = getString(R.string.pleaselogin)
            checkoutButton = rootView.findViewById(R.id.checkoutbutton)
            checkoutButton.visibility = View.GONE
        }
        return rootView
    }
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun checkOut(checkoutbutton: TextView, announce: TextView) {
        try {
            lifecycleScope.launch(Dispatchers.IO) {
                val getUser: String = sharedpreferences.getUserLogged()
                val getUserId = appDatabase.userDao().getIdByUsername(getUser)
                val getBalance: Double = appDatabase.userDao().getBalanceByUsername(getUser)
                val cartlist = appDatabase.cartDao().getCartsByUser(getUserId.toLong())
                totalcart = totalPrice(cartlist)
                if(cartlist.isNotEmpty()){
                    withContext(Dispatchers.Main) {
                        announce.text = "Total: $totalcart"
                        checkoutbutton.setOnClickListener {
                            if (getBalance < totalcart) {
                                Toast.makeText(context, "Not Enough Balance", Toast.LENGTH_SHORT).show()
                            } else {
                                lifecycleScope.launch(Dispatchers.IO) {
                                    for (item in cartlist) {
                                        appDatabase.cartDao().deleteCart(item)
                                    }
                                }
                                Toast.makeText(context, "Checkout Completed", Toast.LENGTH_SHORT).show()
                                checkOut(checkoutbutton, announce)
                                val i = Intent(requireContext(), CheckoutActivity::class.java)
                                startActivity(i)
                            }
                        }
                    }
                }
                else{
                    announce.text = "Your Cart Is Empty"
                }
            }
        } catch (e: Exception) {
            Log.d("Lifecycle Error:", "Error with ${e.message}")
        }
    }
    private fun totalPrice(cartlist: List<Cart>): Double{
        var totalprice = 0.0
        for (cartitem in cartlist){
            totalprice += cartitem.productPrice
        }
        return totalprice
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onDeleteButtonClick(cart: Cart) {
        try {
            lifecycleScope.launch(Dispatchers.IO) {
                appDatabase.cartDao().deleteCart(cart)
                // Notify the adapter that the data set has changed
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Delete From Cart Successfully", Toast.LENGTH_SHORT).show()
                    recyclerViewCart.adapter?.notifyDataSetChanged()
                    checkOut(checkoutButton, announce)
                }
            }
        } catch (e: Exception) {
            Log.d("Error Delete Cart:", e.message.toString())
        }
    }
}