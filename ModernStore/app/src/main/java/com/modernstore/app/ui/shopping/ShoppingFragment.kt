package com.modernstore.app.ui.shopping

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import com.modernstore.app.R
import com.modernstore.app.data.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.shopping_fragment, container, false)
        // searchview
        val searchview : SearchView = view.findViewById(R.id.searchView)
        searchview.isIconified = false
        searchview.setQuery("Search Product", false)
        searchview.clearFocus()
        // test button
        val test : Button = view.findViewById(R.id.testbutton)
        test.setOnClickListener {
            val calling : Call<List<Product>> = fetchData()
        }
        return view
    }
    private fun fetchData(): Call<List<Product>> {
        val apiService = RetrofitClient.apiService
        val call: Call<List<Product>> = apiService.getproducts()
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products: List<Product>? = response.body()
                    products?.let {
                        Log.d("API:", products.toString())
                    }
                } else {
                    Log.d("API:", "ERROR")
                }
            }
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("API-ERROR: ", "Error")
            }
        })
        return call
    }
}