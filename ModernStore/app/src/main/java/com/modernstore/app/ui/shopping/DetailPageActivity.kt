package com.modernstore.app.ui.shopping

import android.annotation.SuppressLint
import com.modernstore.app.data.api.RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.modernstore.app.MainActivity
import com.modernstore.app.R
import com.modernstore.app.data.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPageActivity : AppCompatActivity() {
    private lateinit var categories: String
    private lateinit var adapter: ProductAdapter
    private fun fetchDataCat(callback: (List<Product>?, Throwable?) -> Unit) {
        val apiService = RetrofitClient.apiService
        val call: Call<List<Product>> = apiService.getProductByCat(categories)
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products: List<Product>? = response.body()
                    products?.let {
                        Log.d("API Result:", products.toString())
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
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)
        val productId = intent.getIntExtra("productId", 0)
        categories = intent.getStringExtra("category").toString()
        val imagex = findViewById<ImageView>(R.id.detail_image)
        val titlex : TextView = findViewById(R.id.detail_title)
        val categoryx : TextView = findViewById(R.id.detail_category)
        val pricex : TextView = findViewById(R.id.detail_price)
        val descriptionx : TextView = findViewById(R.id.detail_description)
        val ratingx : TextView = findViewById(R.id.detail_rating)
        // Fetch product details using Retrofit
        val apiService = RetrofitClient.apiService
        val call: Call<Product> = apiService.getProductById(productId)
        call.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    val product: Product? = response.body()
                    product?.let {
                        // Handle the retrieved product details
                        Log.d("Product Details", "ID: ${it.id}, Title: ${it.title}")
                        titlex.text = it.title
                        categoryx.text = it.category
                        pricex.text = it.price.toString()
                        descriptionx.text = it.description
                        ratingx.text = it.rating.toString()
                        Glide.with(this@DetailPageActivity)
                            .load(product.image)
                            .placeholder(R.drawable.dummy)
                            .error(R.drawable.dummy)
                            .centerInside()
                            .into(imagex)
                    }
                } else {
                    Log.e("API Error", "Failed to fetch product details")
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.e("API Error", "Error fetching product details", t)
            }
        })
        val back : Button = findViewById(R.id.from_detail_to_home)
        back.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
        val recyclerviewcat: RecyclerView = findViewById(R.id.recyclerViewCat)
        recyclerviewcat.layoutManager = LinearLayoutManager(this@DetailPageActivity)

        fetchDataCat { products, error ->
            if (error != null) {
                // Handle error
                Log.e("API", "Error: $error")
                Log.d("Categories:", "Show: $categories")
            } else {
                // Set up the adapter with the retrieved list of products
                adapter = ProductAdapter(products ?: emptyList()) { product ->
                    onItemClick(product)
                }
                recyclerviewcat.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
    }
    private fun onItemClick(product: Product) {
        val intent = Intent(this@DetailPageActivity, DetailPageActivity::class.java)
        intent.putExtra("productId", product.id)
        intent.putExtra("category", product.category)
        startActivity(intent)
    }
}
