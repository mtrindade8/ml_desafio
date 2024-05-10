package com.example.mercadoapp.data.repository

import com.example.mercadoapp.domain.models.ProductDetails
import com.example.mercadoapp.domain.models.ProductList
import retrofit2.Response

interface Repository {

    suspend fun getProducts(searchQuery: String): Response<ProductList>

    suspend fun getProductById(productId: String): Response<ProductDetails>
}