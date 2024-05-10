package com.example.mercadoapp.data.repository

import com.example.mercadoapp.data.api.MeliApi
import com.example.mercadoapp.domain.models.ProductDetails
import com.example.mercadoapp.domain.models.ProductList
import retrofit2.Response

//Repository implementation to request data from API
class RepositoryImpl(private val meliApi: MeliApi) : Repository {

    override suspend fun getProducts(searchQuery: String): Response<ProductList> {
        return meliApi.getProducts(searchQuery)
    }

    override suspend fun getProductById(productId: String): Response<ProductDetails> {
        return meliApi.getProductById(productId)
    }

}