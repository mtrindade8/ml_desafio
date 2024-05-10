package com.example.mercadoapp.domain.models

import com.google.gson.annotations.SerializedName

data class Paging(
    val total: Int,
    val offset: Int,
    val limit: Int,
    @SerializedName("primary_results") val primaryResults: Int,
)
