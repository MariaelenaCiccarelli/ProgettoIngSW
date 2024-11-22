package com.danilo.lootmarket.Network

import com.danilo.lootmarket.Auction
import com.danilo.lootmarket.Network.dto.AstaDTO
import com.danilo.lootmarket.Network.dto.DettagliAstaDTO
import com.danilo.lootmarket.Network.dto.MyToken
import com.danilo.lootmarket.Network.dto.OffertaDTO
import com.danilo.lootmarket.Network.dto.UtenteAutenticazioneDTO
import com.danilo.lootmarket.Network.dto.UtenteDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Path

interface MyApi {

    @GET("/getAsteHome")
    suspend fun getAsteHome(@Query("indice") indice: Int, @Query("token") token: String): Response<ArrayList<AstaDTO>>

    @GET("/getDatiUtentePersonali")
    suspend fun getDatiUtentePersonali(@Query("mailUtente") mailUtente: String, @Query("token")  token:String): Response<UtenteDTO>

    @GET("/getDettagliAsta")
    suspend fun getDettagliAsta(@Query("idAsta") id: Int, @Query("mailUtente") mailUtente: String, @Query("token")  token:String): Response<DettagliAstaDTO>

    /*
    @Headers("Content-Type: application/json")
    @POST("/postCreaAsta")
    suspend fun postNuovaAsta(@Body asta: AstaDTO, @Query("token") token: String): Response<Int>


     */
    @Multipart
    @POST("/postModificaUtente")
    suspend fun postModificaUtente(@Part immagineProfiloDTO: MultipartBody.Part, @Part("utenteDTO") utente: UtenteDTO, @Query("token") token: String): Response<Int>

    @POST("/postUpgradeUtente")
    suspend fun postUpgradeUtente(@Body utente: UtenteDTO, @Query("token") token: String): Response<Int>

    @POST("/postRegistraUtente")
    suspend fun postRegistraUtente(@Body utente: UtenteAutenticazioneDTO): Response<MyToken>

    @POST("/postAccediUtente")
    suspend fun postAccediUtente(@Body utente: UtenteAutenticazioneDTO): Response<MyToken>

    @GET("/getAsteUtente")
    suspend fun getAsteUtente(@Query("email") email: String, @Query("token") token: String): Response<ArrayList<AstaDTO>>
    //@Header("Token") token: String,

    @POST("/postIscrizione")
    suspend fun postIscrizione(@Body offertaDTO:OffertaDTO, @Query("token") token: String):Response<Int>

    @POST("/postDisiscrizione")
    suspend fun postDisiscrizione(@Body offertaDTO:OffertaDTO, @Query("token") token: String):Response<Int>

    @POST("/postNuovaOfferta")
    suspend fun postNuovaOfferta(@Body offertaDTO:OffertaDTO, @Query("token") token: String):Response<Int>

    @Multipart
    @POST("/postCreaAsta")
    suspend fun postNuovaAstaConImmagine(@Part immagineProdottoDTO: MultipartBody.Part, @Part("astaDTO") astaDTO: AstaDTO, @Query("token") token: String):Response<Int>
}