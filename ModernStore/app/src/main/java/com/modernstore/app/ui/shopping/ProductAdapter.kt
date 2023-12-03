package com.modernstore.app.ui.shopping

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.modernstore.app.R
import com.modernstore.app.data.model.Product

class ProductAdapter(
    private val products: List<Product>,
    private val clickListener: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            clickListener(product)
        }
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            itemView.findViewById<TextView>(R.id.product_title).text = product.title
            itemView.findViewById<TextView>(R.id.product_price).text = "$${product.price}"
            itemView.findViewById<TextView>(R.id.product_category).text = product.category
            val productImage = itemView.findViewById<ImageView>(R.id.product_image)
            // Load image using Glide
            Glide.with(itemView)
                .load(product.image)
                .placeholder(R.drawable.dummy)
                .error(R.drawable.dummy)
                .centerInside()
                .into(productImage)
        }
    }
}
