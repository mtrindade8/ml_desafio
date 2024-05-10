package com.example.mercadoapp.presentation.productsearch.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mercadoapp.databinding.FragmentProductListBinding
import com.example.mercadoapp.domain.models.Product
import com.example.mercadoapp.presentation.productsearch.adapter.ProductListAdapter
import com.example.mercadoapp.presentation.productsearch.viewmodels.ProductSearchViewModel

private const val TAG = "ProductListFragment"

//Fragment that holds the ProductList Recyclerview and handles the user click on Products.
class ProductListFragment : Fragment() {

    private val viewModel: ProductSearchViewModel by activityViewModels<ProductSearchViewModel>()
    private lateinit var  binding: FragmentProductListBinding
    private lateinit var productListAdapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               requireActivity().finish()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setObservers()
    }

    private fun setObservers() {
        Log.d(TAG, "Setting observers")
        viewModel.productList.observe(viewLifecycleOwner, Observer {
            handleEmptyList(it)
        })
    }

    private fun handleEmptyList(productList: ArrayList<Product>) {
        if (productList.isNotEmpty()) {
            Log.d(TAG, "Updating recyclerview")
            productListAdapter.updateData(productList)
        }
    }

    private fun setRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
        productListAdapter = ProductListAdapter().apply {
            onItemClick = {
                    viewModel.setSelectedProductId(it.id)
            }
        }
        binding.productsRecyclerView.apply {
            this.layoutManager = layoutManager
            this.adapter = productListAdapter
        }
    }
}
