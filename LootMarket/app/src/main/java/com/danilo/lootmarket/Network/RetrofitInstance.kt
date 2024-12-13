package com.danilo.lootmarket.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: MyApi by lazy {
            Retrofit.Builder()
                .baseUrl("http://16.170.212.84:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
    }
}