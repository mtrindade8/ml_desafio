package com.example.mercadoapp.presentation.productsearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mercadoapp.databinding.ListItemProductBinding
import com.example.mercadoapp.domain.models.Product
import com.example.mercadoapp.domain.models.ProductDetails

//Adapter that binds the Products on the Recyclerview.
class ProductListAdapter () : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> () {

    inner class ProductViewHolder(val binding: ListItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    var onItemClick : ((Product) -> Unit)? = null
    private var products: ArrayList<Product> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ListItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        with(holder.binding) {
            with(products[position]){
                titleTextView.text = this.title
                priceTextView.text = ProductDetails.getLocaleCurrency(this.price)
                titleTextView.text = this.title
                Glide.with(holder.itemView).load(this.thumbnail).into(productThumbnail)
                holder.itemView.setOnClickListener {
                    onItemClick?.invoke(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateData(productList: ArrayList<Product>) {
        products.clear()
        products.addAll(productList)
        notifyDataSetChanged()
    }
}