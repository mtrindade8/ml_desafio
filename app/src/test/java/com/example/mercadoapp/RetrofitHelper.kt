package com.example.mercadoapp

import com.example.mercadoapp.data.api.MeliApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    fun meliApiInstance(baseUrl: String): MeliApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MeliApi::class.java)
    }

}