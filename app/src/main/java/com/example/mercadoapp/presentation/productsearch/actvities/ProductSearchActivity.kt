package com.example.mercadoapp.presentation.productsearch.actvities

import android.content.Context

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mercadoapp.R
import com.example.mercadoapp.databinding.ActivityProductSearchBinding
import com.example.mercadoapp.presentation.productsearch.fragments.ProductDetailFragment
import com.example.mercadoapp.presentation.productsearch.fragments.ProductListFragment
import com.example.mercadoapp.presentation.productsearch.viewmodels.ProductSearchViewModel
import com.example.mercadoapp.utils.ConnectivityObserver
import com.example.mercadoapp.utils.NetworkConnectivityObserver
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "ProductSearchActivity"

//Activity that holds the SearchView and handle the fragment's navigation.
class ProductSearchActivity : AppCompatActivity() {

    lateinit var viewModel: ProductSearchViewModel
    private lateinit var  binding: ActivityProductSearchBinding
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var networkSnackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductSearchBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, ProductSearchViewModel.Factory)[ProductSearchViewModel::class.java]
        setContentView(binding.root)
        setObservers()
        setSearchView()
    }

    private fun setObservers() {

        viewModel.onEmptyResponse.observe(this, Observer {
            Toast.makeText(this, getString(R.string.product_not_found_alert), Toast.LENGTH_SHORT).show()
        })

        viewModel.selectedProductId.observe(this, Observer {
            if (!it.isNullOrEmpty()){
                viewModel.getProductById(it)
                viewModel.cleanProductDetails()
                replaceFragment(ProductDetailFragment())
            } else {
                Toast.makeText(this, getString(R.string.product_details_not_found_alert), Toast.LENGTH_SHORT).show()
            }
        })

        setConnectivityObserver()
    }

    private fun setSearchView() {
        binding.searchView.apply {
            setOnClickListener { isIconified = false }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(!query.isNullOrEmpty()) {
                        Log.d(TAG, "Searching for: $query")
                        viewModel.cleanProductList()
                        viewModel.getProducts(query)
                        replaceFragment(ProductListFragment())
                        dismissKeyboard(binding.root)
                        return true
                    }
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun setConnectivityObserver() {
        setConnectivitySnackBar()
        connectivityObserver = NetworkConnectivityObserver(this)
        networkSnackbar.show()
        connectivityObserver.observe().onEach {
            when(it){
                ConnectivityObserver.Status.Available -> networkSnackbar.apply { if (isShown) dismiss() }
                ConnectivityObserver.Status.Unavailable -> networkSnackbar.show()
                ConnectivityObserver.Status.Losing -> Log.d(TAG, "Losing internet connection")
                ConnectivityObserver.Status.Lost -> networkSnackbar.show()
            }
        }.launchIn(lifecycleScope)
    }

    private fun setConnectivitySnackBar() {
        networkSnackbar = Snackbar
            .make(
                findViewById(android.R.id.content), getString(R.string.internet_connection_alert),
                Snackbar.LENGTH_INDEFINITE
            )
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
        fragmentTransaction.replace(binding.navHostFragmentContainer.id, fragment)
        fragmentTransaction.commit()
    }

    fun dismissKeyboard(view: View?) {
        if (view != null) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}