package com.danilo.lootmarket.Network

import com.danilo.lootmarket.Auction
import com.danilo.lootmarket.Network.dto.AstaDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface MyApi {

    @GET("/getAsteHome")
    suspend fun getAsteHome(@Query("indice") indice: Int): Response<ArrayList<AstaDTO>>
}