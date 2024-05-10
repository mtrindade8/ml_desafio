package com.example.mercadoapp.data.api

import com.example.mercadoapp.data.HttpRoutes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(HttpRoutes.BASE_MELI_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val meliApi: MeliApi by lazy {
        retrofit.create(MeliApi::class.java)
    }

}