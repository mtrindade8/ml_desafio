package com.example.mercadoapp.presentation.productsearch.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mercadoapp.data.api.RetrofitInstance
import com.example.mercadoapp.data.repository.RepositoryImpl
import com.example.mercadoapp.domain.models.Product
import com.example.mercadoapp.domain.models.ProductDetails
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "ProductSearchViewModel"

//ViewModel that handles the logic behind product search flow.
class ProductSearchViewModel(private val repositoryImpl: RepositoryImpl) : ViewModel() {

    private val _productList: MutableLiveData<ArrayList<Product>> = MutableLiveData()
    private val _productDetails: MutableLiveData<ProductDetails> = MutableLiveData()
    private val _onEmptyResponse: MutableLiveData<Boolean> = MutableLiveData()
    private val _selectedProductId: MutableLiveData<String> = MutableLiveData()
    val selectedProductId: LiveData<String> get() = _selectedProductId
    val onEmptyResponse: LiveData<Boolean> get() = _onEmptyResponse
    val productList: LiveData<ArrayList<Product>> get() = _productList
    val productDetails: LiveData<ProductDetails> get() = _productDetails

    fun setSelectedProductId(id: String) {
        _selectedProductId.value = id
    }

    fun cleanProductDetails() {
        _productDetails.value = ProductDetails()
    }

    fun cleanProductList() {
        _productList.value = arrayListOf()
    }

    fun getProducts(searchQuery: String) {
        viewModelScope.launch {
            val response = try {
                repositoryImpl.getProducts(searchQuery)
            } catch (e: IOException) {
                Log.e(TAG, "IOException, pode estar relacionado com a conexão da internet.")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, resposta HTTP inexperada.")
                return@launch
            }

            val body = response.body()
            if (response.isSuccessful && body != null && body.results.isNotEmpty()) {
                _productList.value = body.results
            } else {
                _onEmptyResponse.value = true
            }
        }
    }

    fun getProductById(productId: String) {
        viewModelScope.launch {
            val response = try {
                repositoryImpl.getProductById(productId)
            } catch (e: IOException) {
                Log.e(TAG, "IOException, may be related to internet connection.")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected HTTP response.")
                return@launch
            }

            val body = response.body()
            if (response.isSuccessful && body != null) {
                _productDetails.value = body
            } else {
                Log.e(TAG, "Erro ao buscar detalhes do produto.")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return ProductSearchViewModel(
                    RepositoryImpl(RetrofitInstance.meliApi)
                ) as T
            }
        }
    }
}