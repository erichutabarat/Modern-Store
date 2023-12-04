package com.modernstore.app.data.api

import com.modernstore.app.data.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface API {
    @GET("/products")
    fun getproducts(): Call<List<Product>>
    @GET("products/{productId}")
    fun getProductById(@Path("productId") productId: Int): Call<Product>

    @GET("products/category/{cat}")
    fun getProductByCat(@Path("cat") cat: String): Call<List<Product>>
}