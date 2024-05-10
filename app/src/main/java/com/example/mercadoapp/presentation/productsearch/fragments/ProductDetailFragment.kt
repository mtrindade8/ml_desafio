package com.example.mercadoapp.presentation.productsearch.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.mercadoapp.R
import com.example.mercadoapp.databinding.FragmentProductDetailBinding
import com.example.mercadoapp.domain.models.ProductDetails
import com.example.mercadoapp.presentation.productsearch.viewmodels.ProductSearchViewModel

private const val TAG = "ProductDetailFragment"

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
        if (isAdded) {
            binding.apply {
                titleTextView.text = productDetails.title
                priceTextView.text = requireActivity().getString(
                    R.string.product_currency,
                    productDetails.price.toString()
                ).replace(".", ",")
                attributesTextView.text = ProductDetails.getAttributeString(productDetails.attributes)
                Glide.with(requireActivity()).load(productDetails.pictures.first().url).into(productImage)
                container.visibility = View.VISIBLE
            }
        }
    }
}
