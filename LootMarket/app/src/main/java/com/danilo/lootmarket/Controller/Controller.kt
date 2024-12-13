package com.danilo.lootmarket.Controller


import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log

import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat

import com.danilo.lootmarket.view.adapters.AuctionsAdapter
import com.danilo.lootmarket.view.adapters.AuctionsLiveAdapter


import com.danilo.lootmarket.modelview.AuctionView
import com.danilo.lootmarket.modelview.IndirizzoView
import com.danilo.lootmarket.modelview.NotificationView
import com.danilo.lootmarket.modelview.UserBaseView
import com.danilo.lootmarket.modelview.UserBusinessView
import com.danilo.lootmarket.view.adapters.NotificationAdapters
import com.danilo.lootmarket.R
import com.danilo.lootmarket.modelview.AuctionLiveView
import com.danilo.lootmarket.modelview.DettagliAstaView
import com.danilo.lootmarket.network.RetrofitInstance
import com.danilo.lootmarket.network.dto.AstaDTO
import com.danilo.lootmarket.network.dto.DettagliAstaDTO
import com.danilo.lootmarket.network.dto.MyToken
import com.danilo.lootmarket.network.dto.NotificaDTO
import com.danilo.lootmarket.network.dto.OffertaDTO
import com.danilo.lootmarket.network.dto.UtenteAutenticazioneDTO
import com.danilo.lootmarket.network.dto.UtenteDTO

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

import okio.IOException

import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Base64

class Controller{

    @RequiresApi(Build.VERSION_CODES.O)
    public suspend fun getAste(indice: Int, token: String, adapter: AuctionsAdapter): Boolean {
        var auctions2 = ArrayList<AuctionView>()
        val response: Response<ArrayList<AstaDTO>> = try {
            RetrofitInstance.api.getAsteHome(indice, token)
        } catch (e: IOException) {
            Log.e("MyNetwork", "IOException, you might not have internet connection")
            return true
        } catch (e: HttpException) {
            Log.e("MyNetwork", "HttpException, unexpected response")
            return true
        }
        if (response.isSuccessful && response.body() != null) {
            Log.e("MyNetwork", "Response is successful")
            var asteRecuperate: List<AstaDTO> = response.body()!!
            for (asta in asteRecuperate) {
                val immagineAstaByteArrayDecoded = Base64.getDecoder().decode(asta.immagineAsta)
                val immagineAsta = BitmapFactory.decodeByteArray(
                    immagineAstaByteArrayDecoded,
                    0,
                    immagineAstaByteArrayDecoded.size
                )
                var auctionView = AuctionView(
                    asta.idAsta,
                    asta.titolo,
                    asta.ultimaOfferta,
                    ZonedDateTime.of(
                        asta.anno,
                        asta.mese,
                        asta.giorno,
                        0,
                        0,
                        0,
                        0,
                        ZoneId.of("GMT")
                    ),
                    immagineAsta,
                    asta.descrizione,
                    asta.categoria,
                    asta.tipoAsta
                )
                auctions2.add(auctionView)
            }
            adapter.auctionViews.addAll(auctions2)
            adapter.notifyItemRangeInserted(adapter.itemCount + 1, auctions2.size);
            return true
        } else {
            Log.e("MyNetwork", "Response not successful")
            if (response.code().toString().equals("401")) {
                Log.e("MyNetwork", response.code().toString() + " Token Scaduto!")
            }
            return false
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    public suspend fun getAsteUtente(email: String, token: String, liveAdapter: AuctionsLiveAdapter): Boolean{
        var auctionsCaricate = ArrayList<AuctionLiveView>()

            val response = try{
                RetrofitInstance.api.getAsteUtente(email, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return false
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return false
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO,"MyNetwork", "Response is successful")
                var asteRecuperate: List<AstaDTO> = response.body()!!
                for(asta in asteRecuperate){
                    val immagineAstaByteArrayDecoded = Base64.getDecoder().decode(asta.immagineAsta)
                    val immagineAsta= BitmapFactory.decodeByteArray(immagineAstaByteArrayDecoded, 0, immagineAstaByteArrayDecoded.size)
                    var auctionLiveView = AuctionLiveView(asta.idAsta, asta.titolo, asta.ultimaOfferta, ZonedDateTime.of(asta.anno, asta.mese, asta.giorno, 0, 0, 0,0, ZoneId.of("GMT")), immagineAsta, asta.emailCreatore, asta.offertaFatta)
                    auctionsCaricate.add(auctionLiveView)
                }
                //arrayAsteUtente = arrayAsteUtente + auctionsCaricate
                liveAdapter.auctionsViewHistory = auctionsCaricate
                liveAdapter!!.notifyDataSetChanged()
                return true


            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                return false
            }
    }

    public suspend fun creaAsta(
        mail: String,
        titolo: String,
        categoria: String,
        prezzo: String,
        momentoScadenza: ZonedDateTime,
        descrizione: String,
        sogliaMinima: String,
        tipo: String,
        fileDir: File,
        inputStream: InputStream,
        token: String
        ): Boolean{
            val imageFile = File(fileDir, "immagineProdotto.png")
            val outputStream = FileOutputStream(imageFile)
            inputStream!!.copyTo((outputStream))
            val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val partImmagineDTO =
                MultipartBody.Part.createFormData("immagineProdottoDTO", imageFile.name, requestBody)
            var astaDTO = AstaDTO(
                0,
                mail,
                titolo,
                categoria,
                prezzo.toDouble(),
                momentoScadenza.year,
                momentoScadenza.month.value,
                momentoScadenza.dayOfMonth,
                descrizione,
                prezzo.toDouble(),
                sogliaMinima.toDouble(),
                tipo,
                false,
                "immagineInMultiPart"
            )
            return postAsta(partImmagineDTO, astaDTO, token)
    }


    private suspend fun postAsta(partImmagine: MultipartBody.Part, astaDTO: AstaDTO, token: String): Boolean{

        val response = try{
            RetrofitInstance.api.postNuovaAstaConImmagine(partImmagine, astaDTO, token)
        }catch (e: IOException){
            Log.e("MyNetwork", "IOException, you might not have internet connection")
            return false
        }catch (e: HttpException){
            Log.e("MyNetwork", "HttpException, unexpected response")
            return false
        }catch (e: Exception){
            Log.e("MyNetwork", e.message.toString())
            Log.e("MyNetwork", e.cause.toString())
            return false
        }
        if(response.isSuccessful && response.body() != null){
            Log.e("MyNetwork", "Response is successful")
            return true
        }else{
            Log.e("MyNetwork", "Response not successful")
            if(response.code().toString().equals("401")){
                Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
            }
            return false
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    public suspend fun getDettagliAsta(id: Int, mailUtente: String, token: String): Pair<DettagliAstaView?, Bitmap?>{
            val response = try{
                RetrofitInstance.api.getDettagliAsta(id, mailUtente, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return Pair(null, null)
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return Pair(null, null)
            }catch (e: Exception){
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return Pair(null, null)
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO, "MyNetwork", "Response is successful")
                var astaRecuperata: DettagliAstaDTO = response.body()!!
                val immagineAstaByteArrayDecoded = Base64.getDecoder().decode(astaRecuperata.immagineAsta)
                try{
                    var immagineAsta= BitmapFactory.decodeByteArray(immagineAstaByteArrayDecoded, 0, immagineAstaByteArrayDecoded.size)
                    var dataScadenza: ZonedDateTime = ZonedDateTime.of(astaRecuperata.anno, astaRecuperata.mese, astaRecuperata.giorno, 0, 0, 0,0, ZoneId.of("GMT"))
                    var astaRecuperataView = DettagliAstaView(astaRecuperata.idAsta, astaRecuperata.emailCreatore, astaRecuperata.titolo, astaRecuperata.categoria, astaRecuperata.prezzoPartenza, dataScadenza, astaRecuperata.descrizione, astaRecuperata.ultimaOfferta, astaRecuperata.sogliaMinima, astaRecuperata.tipoAsta, astaRecuperata.ultimaOffertaTua, astaRecuperata.immagineAsta, astaRecuperata.nomeAutore, astaRecuperata.statusLegame)
                    return Pair(astaRecuperataView, immagineAsta)
                }catch (e: Exception){
                    Log.e("MyNetwork", e.toString())
                    Log.e("MyNetwork", e.message.toString())
                    Log.e("MyNetwork", e.cause.toString())
                    return Pair(null, null)
                }
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                return Pair(null, null)
            }
        }





    public suspend fun postIscrizione(idAsta: Int, mailUtente: String, token: String) : Boolean{
        val offertaDTO = OffertaDTO(idAsta, mailUtente, -1.0)

            val response = try{
                RetrofitInstance.api.postIscrizione(offertaDTO, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return false

            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return false
            }catch (e: Exception){
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return false
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO, "MyNetwork", "Response is successful")
                if(response.body()==1){
                    return true
                }
                else{
                    return false
                }
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                return false
            }
        }





    public suspend fun postDisiscrizione(idAsta: Int, mailUtente: String, token: String): Boolean {
    val offertaDTO = OffertaDTO(idAsta, mailUtente, -1.0)
    val response = try {
        RetrofitInstance.api.postDisiscrizione(offertaDTO, token)
    } catch (e: IOException) {
        Log.e("MyNetwork", "IOException, you might not have internet connection")
        return false
    } catch (e: HttpException) {
        Log.e("MyNetwork", "HttpException, unexpected response")
        return false
    } catch (e: Exception) {
        Log.e("MyNetwork", e.toString())
        Log.e("MyNetwork", e.message.toString())
        Log.e("MyNetwork", e.cause.toString())
        return false
    }
    if (response.isSuccessful && response.body() != null) {
        Log.println(Log.INFO, "MyNetwork", "Response is successful")
        if (response.body() == 1) {
            return true

            } else {
             return false
            }
      } else {
          Log.e("MyNetwork", "Response not successful")
         if (response.code().toString().equals("401")) {
                Log.e("MyNetwork", response.code().toString() + " Token Scaduto!")
            }
            return false
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    public suspend fun postNuovaOfferta(idAsta: Int, mailUtente: String, offerta: Double, token: String): Int{
        var offertaDTO = OffertaDTO(idAsta, mailUtente, offerta)

            val response = try{
                RetrofitInstance.api.postNuovaOfferta(offertaDTO, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return -1
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return -1
            }catch (e: Exception){
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return -1
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO, "MyNetwork", "Response is successful")
                if(response.body()==1){
                    return 1
                }
                else if(response.body()==-2){
                    return -2
                }
                else{
                    return -1
                }
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                return -3
            }
        }





    public suspend fun postRegistraUtente(nome: String, cognome: String, codiceFiscale: String, password: String,  email: String, dataNascitaYear:Int, dataNascitaMonthValue:Int, dataNascitaDayOfMonth:Int, nazione: String, cellulare: String): Pair<Int,String> {
        var utenteAutenticazioneDTO = UtenteAutenticazioneDTO(nome, cognome, codiceFiscale, password,  email, dataNascitaYear, dataNascitaMonthValue, dataNascitaDayOfMonth, nazione, cellulare)
        val response = try {
            RetrofitInstance.api.postRegistraUtente(utenteAutenticazioneDTO)
        } catch (e: IOException) {
            Log.e("MyNetwork", e.message.toString())
            Log.e("MyNetwork", e.cause.toString())
            return Pair(-1, "")
        } catch (e: HttpException) {
            Log.e("MyNetwork", "HttpException, unexpected response")

            return Pair(-1, "")
        }
        if (response.isSuccessful && response.body() != null) {
            Log.e("MyNetwork", "Response is successful")
            var jwtToken: MyToken = response.body()!!
            return Pair(1, jwtToken.token)
        } else {
            Log.e("MyNetwork", "Response not successful")
            if (response.errorBody().toString() == "2") {
               return Pair(-2, "")
            }
            return Pair(-1, "")
        }
    }




    public suspend fun postAccediUtente(passwordUtente:String, mailUtente:String):Pair<Boolean, MyToken?>{
            var utenteAutenticazioneDTO = UtenteAutenticazioneDTO("", "", "", passwordUtente, mailUtente, 0, 0, 0,"", "" )
            val response = try{
                RetrofitInstance.api.postAccediUtente(utenteAutenticazioneDTO)
            }catch (e: IOException){
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return Pair(false, null)
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return Pair(false, null)
            }catch (e: Exception){
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.cause.toString())
                return Pair(false, null)
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO, "MyNetwork", "Response is successful")
                var jwtToken: MyToken = response.body()!!
                return Pair(true, jwtToken)


            }else{
                Log.e("MyNetwork", "Response not successful")

                return Pair(false, null)
            }
    }




    public suspend fun getNotificheUtente(email: String, token: String, adapter: NotificationAdapters, resources: Resources): Boolean{
        var notificheCaricate = ArrayList<NotificationView>()

            val response = try{
                RetrofitInstance.api.getNotificheUtente(email, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return false
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return false
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO,"MyNetwork", "Response is successful")
                var notificheRecuperate: List<NotificaDTO> = response.body()!!
                for(notifica in notificheRecuperate){
                    var testoNotifica = notifica.getMessaggio()
                    var titoloNotifica = notifica.getTitolo()
                    var immagineNotifica: Drawable
                    when(notifica.tipo) {
                        1-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_heart_broken_24, null)!!
                        2-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_celebration_24, null)!!
                        3-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_hourglass_bottom_24, null)!!
                        4-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_auto_awesome_24, null)!!
                        5-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_gavel_24_notific, null)!!
                        6-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_hail_24, null)!!
                        7-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_mood_bad_24, null)!!
                        8-> immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_heart_broken_24, null)!!
                        else ->{
                            immagineNotifica = ResourcesCompat.getDrawable(resources, R.drawable.baseline_report_gmailerrorred_24, null)!!
                        }
                    }
                    var notificationView = NotificationView(notifica.id, notifica.idAsta, immagineNotifica, titoloNotifica, testoNotifica)
                    notificheCaricate.add(notificationView)
                }
                adapter.notificationViewViewHistory = notificheCaricate
                adapter!!.notifyDataSetChanged()
                return true
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                return false
            }
    }





    public suspend fun postEliminaNotificaUtente(idNotifica: Int, idAsta: Int, token: String): Boolean{
            val response = try{
                RetrofitInstance.api.postEliminaNotifica(idNotifica, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return false
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return false
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO,"MyNetwork", "Response is successful")

                return true
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }

                return false
            }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    public suspend fun getDatiUtenteTerzi(mailUtenteTerzo: String, token:String): UserBusinessView?{

            val response = try{
                RetrofitInstance.api.getDatiUtenteTerzi(mailUtenteTerzo, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return null
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return null
            }catch (e: Exception){
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return null
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO, "MyNetwork", "Response is successful")
                var utenteRecuperato: UtenteDTO = response.body()!!
                val immagineProfiloByteArrayDecoded = Base64.getDecoder().decode(utenteRecuperato.immagineProfilo)
                try{
                    var immagineProfilo= BitmapFactory.decodeByteArray(immagineProfiloByteArrayDecoded, 0, immagineProfiloByteArrayDecoded.size)
                    var indirizzoViewPlaceHolder = IndirizzoView("","", "", "")
                    var utenteBase = UserBaseView(immagineProfilo, utenteRecuperato.nome, "", utenteRecuperato.mail, LocalDate.of(1970,1,1), utenteRecuperato.nazione, "", indirizzoViewPlaceHolder, indirizzoViewPlaceHolder, utenteRecuperato.sito, utenteRecuperato.socialFacebook, utenteRecuperato.socialInstagram, utenteRecuperato.biografia)
                    var utente: UserBusinessView
                    if(!utenteRecuperato.ragioneSociale.equals("")){
                        var utenteBusiness = UserBusinessView(utenteBase, utenteRecuperato.ragioneSociale, utenteRecuperato.partitaIva, utenteRecuperato.numeroAziendale)
                        utente = utenteBusiness
                    }else{
                        utente = UserBusinessView(utenteBase, "", "", "")
                    }
                    //getAsteUtenteTerzi(mailUtente, mailUtenteTerzo)
                    return utente
                }catch (e: Exception){
                    Log.e("MyNetwork", e.toString())
                    Log.e("MyNetwork", e.message.toString())
                    Log.e("MyNetwork", e.cause.toString())
                    return null
                }
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                return null
            }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    public suspend fun getAsteUtenteTerzi(email: String, mailUtenteTerzo: String, token: String, adapter: AuctionsLiveAdapter): Boolean{
        var auctionsCaricate = ArrayList<AuctionLiveView>()
            val response = try{
                RetrofitInstance.api.getAsteUtenteTerzi(email, mailUtenteTerzo, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return false
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return false
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO,"MyNetwork", "Response is successful")
                var asteRecuperate: List<AstaDTO> = response.body()!!
                for(asta in asteRecuperate){
                    val immagineAstaByteArrayDecoded = Base64.getDecoder().decode(asta.immagineAsta)
                    val immagineAsta= BitmapFactory.decodeByteArray(immagineAstaByteArrayDecoded, 0, immagineAstaByteArrayDecoded.size)
                    var auctionLiveView = AuctionLiveView(asta.idAsta, asta.titolo, asta.ultimaOfferta, ZonedDateTime.of(asta.anno, asta.mese, asta.giorno, 0, 0, 0,0, ZoneId.of("GMT")), immagineAsta, asta.emailCreatore, asta.offertaFatta)
                    auctionsCaricate.add(auctionLiveView)
                }

                adapter.auctionsViewHistory = auctionsCaricate
                adapter!!.notifyDataSetChanged()
                return true
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                return false
            }
    }




    //Carica i dati personali dell'utente dal backend
    @RequiresApi(Build.VERSION_CODES.O)
    public suspend fun getDatiUtentePersonali(mailUtente: String, token: String): UserBusinessView?{
            val response = try{
                RetrofitInstance.api.getDatiUtentePersonali(mailUtente, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return null
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return null
            }catch (e: Exception){
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return null
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO, "MyNetwork", "Response is successful")
                var utenteRecuperato: UtenteDTO = response.body()!!
                var dataNascita = LocalDate.of(utenteRecuperato.dataDiNascitaAnno, utenteRecuperato.dataDiNascitaMese, utenteRecuperato.dataDiNascitaGiorno)
                val immagineProfiloByteArrayDecoded = Base64.getDecoder().decode(utenteRecuperato.immagineProfilo)
                try{
                    var immagineProfilo= BitmapFactory.decodeByteArray(immagineProfiloByteArrayDecoded, 0, immagineProfiloByteArrayDecoded.size)

                    var utenteBase = UserBaseView(immagineProfilo, utenteRecuperato.nome, utenteRecuperato.codiceFiscale, utenteRecuperato.mail, dataNascita, utenteRecuperato.nazione, utenteRecuperato.numeroCellulare, utenteRecuperato.indirizzoSpedizione, utenteRecuperato.indirizzoFatturazione, utenteRecuperato.sito, utenteRecuperato.socialFacebook, utenteRecuperato.socialInstagram, utenteRecuperato.biografia)
                    var utente: UserBusinessView
                    if(!utenteRecuperato.ragioneSociale.equals("")){
                        var utenteBusiness = UserBusinessView(utenteBase, utenteRecuperato.ragioneSociale, utenteRecuperato.partitaIva, utenteRecuperato.numeroAziendale)
                        utente = utenteBusiness
                    }else{

                        utente = UserBusinessView(utenteBase, "", "", "")
                    }
                    return utente
                }catch (e: Exception){
                    Log.e("MyNetwork", e.toString())
                    Log.e("MyNetwork", e.message.toString())
                    Log.e("MyNetwork", e.cause.toString())
                    return null
                }
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }

                return null
            }
        }





    @RequiresApi(Build.VERSION_CODES.O)
    public  suspend fun postModificaUtente(partImmagine: MultipartBody.Part, utente: UserBusinessView, nazione: String, numeroCellulare: String, indirizzoViewSpedizione: IndirizzoView, indirizzoViewFatturazione: IndirizzoView, sito: String, socialFacebook: String, socialInstagram: String, biografia: String, numeroAziendale: String, token:String): Boolean{
        var utenteDTO = UtenteDTO(utente.informazioniBase.nome, utente.informazioniBase.codiceFiscale, utente.informazioniBase.mail, utente.informazioniBase.dataDiNascita.year, utente.informazioniBase.dataDiNascita.monthValue, utente.informazioniBase.dataDiNascita.dayOfMonth, nazione, numeroCellulare,indirizzoViewSpedizione, indirizzoViewFatturazione, sito, socialFacebook, socialInstagram, biografia, utente.ragioneSociale, utente.partitaIva, numeroAziendale,"immagineInMultiPart")
            val response = try{
                RetrofitInstance.api.postModificaUtente(partImmagine, utenteDTO, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return false
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return false
            }catch (e: Exception){
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return false
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO,"MyNetwork", "Response is successful")
                return true
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                if(response.code().toString().equals("500")){
                    Log.e("MyNetwork", response.code().toString()+" Modifica utente fallita!")
                }
                return false
            }
    }





    public suspend fun postUpgradeUtente(utente: UserBusinessView, nazione: String, numeroCellulare: String, indirizzoViewSpedizione: IndirizzoView, indirizzoViewFatturazione: IndirizzoView, sito: String, socialFacebook: String, socialInstagram: String, biografia: String, numeroAziendale: String, token: String): Boolean{
        var utenteDTO = UtenteDTO(utente.informazioniBase.nome, utente.informazioniBase.codiceFiscale, utente.informazioniBase.mail, utente.informazioniBase.dataDiNascita.year, utente.informazioniBase.dataDiNascita.monthValue, utente.informazioniBase.dataDiNascita.dayOfMonth, nazione, numeroCellulare,indirizzoViewSpedizione, indirizzoViewFatturazione, sito, socialFacebook, socialInstagram, biografia, utente.ragioneSociale, utente.partitaIva, numeroAziendale,"immagineInMultiPart")
            val response = try{
                RetrofitInstance.api.postUpgradeUtente(utenteDTO, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                Log.e("MyNetwork", e.toString())
                return false
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return false
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO,"MyNetwork", "Response is successful")

                return true
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                if(response.code().toString().equals("500")){
                    Log.e("MyNetwork", response.code().toString()+" Upgrade utente fallito!")
                }
                return false
            }
    }





    @RequiresApi(Build.VERSION_CODES.O)
    public suspend fun getAsteSearch(indice: Int, token: String): List<AuctionView> {
        var auctions = ArrayList<AuctionView>()
        val response: Response<ArrayList<AstaDTO>> = try {
            RetrofitInstance.api.getAsteHome(indice, token)
        } catch (e: IOException) {
            Log.e("MyNetwork", "IOException, you might not have internet connection")
            return listOf()
        } catch (e: HttpException) {
            Log.e("MyNetwork", "HttpException, unexpected response")
            return listOf()
        }
        if (response.isSuccessful && response.body() != null) {
            Log.e("MyNetwork", "Response is successful")
            var asteRecuperate: List<AstaDTO> = response.body()!!
            for (asta in asteRecuperate) {
                val immagineAstaByteArrayDecoded = Base64.getDecoder().decode(asta.immagineAsta)
                val immagineAsta = BitmapFactory.decodeByteArray(
                    immagineAstaByteArrayDecoded,
                    0,
                    immagineAstaByteArrayDecoded.size
                )
                var auctionView = AuctionView(
                    asta.idAsta,
                    asta.titolo,
                    asta.ultimaOfferta,
                    ZonedDateTime.of(
                        asta.anno,
                        asta.mese,
                        asta.giorno,
                        0,
                        0,
                        0,
                        0,
                        ZoneId.of("GMT")
                    ),
                    immagineAsta,
                    asta.descrizione,
                    asta.categoria,
                    asta.tipoAsta
                )
                auctions.add(auctionView)
            }

            return auctions
        } else {
            Log.e("MyNetwork", "Response not successful")
            if (response.code().toString().equals("401")) {
                Log.e("MyNetwork", response.code().toString() + " Token Scaduto!")
            }
            return listOf()
        }
    }




    public suspend fun getEsisteUtente(email: String, password: String): Int{
            val response = try{
                RetrofitInstance.api.getEsisteUtente(email)
            }catch (e: IOException){
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return -1
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return -1
            }
            if(response.isSuccessful && response.body() != null){
                Log.e("MyNetwork", "Response is successful")
                var status = response.body()
                if(status==1){
                    return -2
                }else{
                    return 1
                }
            }else{
                return -1
            }
    }
}
