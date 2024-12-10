package com.danilo.lootmarket.network

import com.danilo.lootmarket.network.dto.AstaDTO
import com.danilo.lootmarket.network.dto.DettagliAstaDTO
import com.danilo.lootmarket.network.dto.MyToken
import com.danilo.lootmarket.network.dto.OffertaDTO
import com.danilo.lootmarket.network.dto.UtenteAutenticazioneDTO
import com.danilo.lootmarket.network.dto.UtenteDTO
import com.danilo.lootmarket.network.dto.NotificaDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface MyApi {

    @GET("/getAsteHome")
    suspend fun getAsteHome(@Query("indice") indice: Int, @Query("token") token: String): Response<ArrayList<AstaDTO>>

    @GET("/getDatiUtentePersonali")
    suspend fun getDatiUtentePersonali(@Query("mailUtente") mailUtente: String, @Query("token")  token:String): Response<UtenteDTO>

    @GET("/getDatiUtenteTerzi")
    suspend fun getDatiUtenteTerzi(@Query("mailUtente") mailUtente: String, @Query("token")  token:String): Response<UtenteDTO>

    @GET("/getDettagliAsta")
    suspend fun getDettagliAsta(@Query("idAsta") id: Int, @Query("mailUtente") mailUtente: String, @Query("token")  token:String): Response<DettagliAstaDTO>

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

    @GET("/getAsteUtenteTerzi")
    suspend fun getAsteUtenteTerzi(@Query("emailUtente") emailUtente: String, @Query("emailUtenteTerzi") emailTerzi: String, @Query("token") token: String): Response<ArrayList<AstaDTO>>

    @POST("/postIscrizione")
    suspend fun postIscrizione(@Body offertaDTO:OffertaDTO, @Query("token") token: String):Response<Int>

    @POST("/postDisiscrizione")
    suspend fun postDisiscrizione(@Body offertaDTO:OffertaDTO, @Query("token") token: String):Response<Int>

    @POST("/postNuovaOfferta")
    suspend fun postNuovaOfferta(@Body offertaDTO:OffertaDTO, @Query("token") token: String):Response<Int>

    @Multipart
    @POST("/postCreaAsta")
    suspend fun postNuovaAstaConImmagine(@Part immagineProdottoDTO: MultipartBody.Part, @Part("astaDTO") astaDTO: AstaDTO, @Query("token") token: String):Response<Int>

    @GET("/getNotificheUtente")
    suspend fun getNotificheUtente(@Query("email") email: String, @Query("token") token: String): Response<ArrayList<NotificaDTO>>

    @GET("/getEsisteUtente")
    suspend fun getEsisteUtente(@Query("email") email: String): Response<Int>

    @POST("/postEliminaNotifica")
    suspend fun postEliminaNotifica(@Query("idNotifica") idNotifica:Int, @Query("token") token: String):Response<Int>

}