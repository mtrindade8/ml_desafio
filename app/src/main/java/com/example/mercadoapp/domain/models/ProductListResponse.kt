package com.example.mercadoapp.domain.models

import com.google.gson.annotations.SerializedName

data class ProductListResponse(
    @SerializedName("results")
    val results: ArrayList<Product>
)
