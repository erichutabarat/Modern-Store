package com.modernstore.app.ui.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.modernstore.app.R
import com.modernstore.app.db.roomdb.Cart

interface CartAdapterListener{
    fun onDeleteButtonClick(cart: Cart)
}
class CartAdapter(private var cartList: MutableList<Cart>, private var listener: CartAdapterListener) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.cart_product_image)
        val productIdTextView: TextView = itemView.findViewById(R.id.cart_product_title)
        val productPriceTextView: TextView = itemView.findViewById(R.id.cart_product_price)
        val deleteButton: Button = itemView.findViewById(R.id.delete_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_product, parent, false)
        return CartViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartList[position]
        holder.productIdTextView.text = currentItem.title
        holder.productPriceTextView.text = currentItem.productPrice.toString()
        Glide.with(holder.itemView.context)
            .load(currentItem.imageSrc)
            .placeholder(R.drawable.dummy)
            .error(R.drawable.dummy)
            .into(holder.productImage)
        holder.deleteButton.setOnClickListener {
            listener.onDeleteButtonClick(currentItem)
            cartList.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }
}
