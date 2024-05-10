package com.example.mercadoapp.domain.models

import java.text.NumberFormat
import java.util.Locale

data class ProductDetails(
    val id: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val pictures: List<ProductPicture> = listOf(),
    val attributes: List<ProductAttribute> = listOf()
) {

    companion object {
        fun getAttributeString(attributesList: List<ProductAttribute>) : String {
            var attributeString = "Características do produto:\n"
            attributesList.forEach {
                if (!it.name.isNullOrEmpty() && !it.valueName.isNullOrEmpty()) {
                    attributeString += "\n${it.name}: ${it.valueName}"
                }
            }
            return attributeString
        }

        fun getLocaleCurrency(double: Double) : String {
            val ptBr = Locale("pt", "BR")
            return NumberFormat.getCurrencyInstance(ptBr).format(double)
        }
    }
}
