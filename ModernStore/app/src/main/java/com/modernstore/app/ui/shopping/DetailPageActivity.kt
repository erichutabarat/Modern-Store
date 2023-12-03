package com.modernstore.app.ui.shopping

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.modernstore.app.MainActivity
import com.modernstore.app.R
import com.modernstore.app.data.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)

        val productId = intent.getIntExtra("productId", 0)
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
    }
}
