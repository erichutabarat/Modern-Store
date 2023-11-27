package com.modernstore.app.ui.shopping

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.modernstore.app.R
import com.modernstore.app.data.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingFragment : Fragment() {
    lateinit var recyclerView : RecyclerView
    private fun fetchData(callback: (List<Product>?, Throwable?) -> Unit) {
        val apiService = RetrofitClient.apiService
        val call: Call<List<Product>> = apiService.getproducts()

        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products: List<Product>? = response.body()
                    products?.let {
                        Log.d("API:", products.toString())
                        callback(products, null)
                    }
                } else {
                    Log.d("API:", "ERROR")
                    callback(null, RuntimeException("Failed to fetch products"))
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("API-ERROR: ", "Error")
                callback(null, t)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.shopping_fragment, container, false)
        // searchview
        val searchview: SearchView = view.findViewById(R.id.searchView)
        searchview.isIconified = false
        searchview.setQuery("Search Product", false)
        searchview.clearFocus()
        // recyclerview
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Call fetchData and set up the adapter inside the callback
        fetchData { products, error ->
            if (error != null) {
                // Handle error
                Log.e("API", "Error: $error")
            } else {
                // Set up the adapter with the retrieved list of products
                val adapter = ProductAdapter(products ?: emptyList())
                recyclerView.adapter = adapter
            }
        }

        return view
    }
}