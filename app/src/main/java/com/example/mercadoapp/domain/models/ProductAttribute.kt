package com.example.mercadoapp.domain.models

import com.google.gson.annotations.SerializedName

data class ProductAttribute(
    val name: String,
    @SerializedName("value_name") val valueName: String
) {
}
