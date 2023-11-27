package com.modernstore.app.data.api

import com.modernstore.app.data.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("/products")
    fun getproducts(): Call<List<Product>>
}