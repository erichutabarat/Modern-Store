package com.modernstore.app.ui.shopping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.modernstore.app.R
import com.modernstore.app.data.model.Product

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) {
            itemView.findViewById<TextView>(R.id.product_title).text = product.title
            itemView.findViewById<TextView>(R.id.product_price).text = "$${product.price}"
            // Load image using your preferred image-loading library (e.g., Glide, Picasso)
            // itemView.findViewById<ImageView>(R.id.productImage)
        }
    }
}