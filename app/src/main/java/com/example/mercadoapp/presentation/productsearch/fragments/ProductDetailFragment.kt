package com.example.mercadoapp.presentation.productsearch.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.mercadoapp.databinding.FragmentProductDetailBinding
import com.example.mercadoapp.domain.models.ProductDetails
import com.example.mercadoapp.presentation.productsearch.viewmodels.ProductSearchViewModel

private const val TAG = "ProductDetailFragment"

//Fragment that shows the details from the selected product
class ProductDetailFragment : Fragment() {

    private val viewModel: ProductSearchViewModel by activityViewModels<ProductSearchViewModel>()
    private lateinit var  binding: FragmentProductDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        Log.d(TAG,"Setting observers")
       viewModel.productDetails.observe(viewLifecycleOwner, Observer {
            validateProductDetails(it)
        })
    }

    private fun validateProductDetails(productDetails: ProductDetails) {
        if (productDetails.id.isNotEmpty()) {
            loadProductData(productDetails)
        }
    }

    private fun loadProductData(productDetails: ProductDetails) {
        Log.d(TAG,"Loading product details data")
        if (isAdded) {
            binding.apply {
                titleTextView.text = productDetails.title
                priceTextView.text = ProductDetails.getLocaleCurrency(productDetails.price)
                attributesTextView.text = ProductDetails.getAttributeString(productDetails.attributes)
                Glide.with(requireActivity()).load(productDetails.pictures.first().url).into(productImage)
                container.visibility = View.VISIBLE
            }
        }
    }
}
