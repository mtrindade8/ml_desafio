package com.example.mercadoapp.data.api

import com.example.mercadoapp.domain.models.ProductDetails
import com.example.mercadoapp.domain.models.ProductList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Interface that handle network calls to Meli endpoints.
interface MeliApi {

    @GET("/sites/MLB/search")
    suspend fun getProducts(@Query("q") searchQuery: String): Response<ProductList>

    @GET("/items/{id}")
    suspend fun getProductById(@Path("id") productId: String): Response<ProductDetails>
}