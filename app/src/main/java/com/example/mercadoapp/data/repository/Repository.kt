package com.example.mercadoapp.data.repository

import com.example.mercadoapp.domain.models.ProductDetails
import com.example.mercadoapp.domain.models.ProductListResponse
import retrofit2.Response

interface Repository {

    suspend fun getProducts(searchQuery: String): Response<ProductListResponse>

    suspend fun getProductById(productId: String): Response<ProductDetails>
}