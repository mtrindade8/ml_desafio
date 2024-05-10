package com.example.mercadoapp

import com.example.mercadoapp.data.api.MeliApi
import retrofit2.Retrofit

object RetrofitHelper {

    fun meliApiInstance(baseUrl: String): MeliApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
            .create(MeliApi::class.java)
    }

}