package com.modernstore.app.ui.shopping

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.modernstore.app.R
import com.modernstore.app.data.api.RetrofitClient
import com.modernstore.app.data.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.shopping_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val searchSubmit: TextView = view.findViewById(R.id.submit_search)
        val searchKeywords: TextView = view.findViewById(R.id.searchkeyword)

        searchSubmit.setOnClickListener {
            val keyword = searchKeywords.text.toString()
            if (keyword.isNotEmpty()) {
                isSearching = true
                searchProducts(keyword) { products, error ->
                    handleProductsResponse(products, error)
                }
            } else {
                fetchData { products, error ->
                    handleProductsResponse(products, error)
                }
            }
            closeKeyboard(searchKeywords)
        }

        // Initial data fetch
        fetchData { products, error ->
            handleProductsResponse(products, error)
        }

        return view
    }

    private fun handleProductsResponse(products: List<Product>?, error: Throwable?) {
        if (error != null) {
            // Handle error
            Log.e("API", "Error: $error")
        } else {
            // Set up the adapter with the retrieved list of products
            val adapter = ProductAdapter(products ?: emptyList()) { product ->
                onItemClick(product)
            }
            recyclerView.adapter = adapter
        }
    }

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

    private fun searchProducts(keyword: String, callback: (List<Product>?, Throwable?) -> Unit) {
        val apiService = RetrofitClient.apiService
        val call: Call<List<Product>> = apiService.getproducts()

        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val allProducts: List<Product>? = response.body()

                    // Filter products based on the keyword
                    val filteredProducts = allProducts?.filter { product ->
                        product.title.contains(keyword, ignoreCase = true) ||
                                product.description.contains(keyword, ignoreCase = true)
                    }

                    filteredProducts?.let {
                        Log.d("API:", it.toString())
                        callback(it, null)
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


    private fun onItemClick(product: Product) {
        val intent = Intent(this@ShoppingFragment.context, DetailPageActivity::class.java)
        intent.putExtra("productId", product.id)
        intent.putExtra("category", product.category)
        startActivity(intent)
    }

    private fun closeKeyboard(view: View) {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
