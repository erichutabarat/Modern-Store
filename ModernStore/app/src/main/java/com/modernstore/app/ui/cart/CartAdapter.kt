package com.modernstore.app.ui.cart

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.modernstore.app.R
import com.modernstore.app.db.roomdb.Cart

class CartAdapter(private val cartList: List<Cart>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.cart_product_image)
        val productIdTextView: TextView = itemView.findViewById(R.id.cart_product_title)
        val productPriceTextView: TextView = itemView.findViewById(R.id.cart_product_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_product, parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartList[position]
        holder.productIdTextView.text = currentItem.title
        holder.productPriceTextView.text = currentItem.productPrice.toString()
        Glide.with(holder.itemView.context)
            .load(currentItem.imageSrc)
            .placeholder(R.drawable.dummy)
            .error(R.drawable.dummy)
            .into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }
}
