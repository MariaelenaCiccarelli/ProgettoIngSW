package com.danilo.lootmarket.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: MyApi by lazy {
            Retrofit.Builder()
                .baseUrl("http://192.168.3.206:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
    }
}