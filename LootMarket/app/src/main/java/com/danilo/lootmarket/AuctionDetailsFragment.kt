package com.danilo.lootmarket
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Network.RetrofitInstance
import com.danilo.lootmarket.Network.dto.AstaDTO
import com.danilo.lootmarket.Network.dto.DettagliAstaDTO
import com.danilo.lootmarket.Network.dto.OffertaDTO
import com.danilo.lootmarket.Network.dto.UtenteDTO
import com.danilo.lootmarket.databinding.FragmentAuctionDetailsBinding
import com.danilo.lootmarket.databinding.FragmentHomeBinding
import kotlinx.coroutines.async
import okio.IOException
import retrofit2.HttpException
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Base64
import java.util.Date
import java.util.Locale


class AuctionDetailsFragment(private var idAuction: Int, private var mailUtente: String, private var token: String): Fragment() {

    private lateinit var binding: FragmentAuctionDetailsBinding
    //private lateinit var navController: NavController


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment


        binding = FragmentAuctionDetailsBinding.inflate(layoutInflater)
        binding.frammentoIntero.isVisible=false
        getDettagliAsta(idAuction,mailUtente,token)
        //val view = inflater.inflate(R.layout.fragment_home, container, false)


        binding.cardBackButtonFrammentoAuctionDetails.setOnClickListener{
            parentFragmentManager.popBackStack()

        }

        binding.cardIscrizioneAstaFrammentoAuctionDetails.setOnClickListener{
            postIscrizione(idAuction, mailUtente, token)
        }

        binding.cardDisiscrizioneAstaFrammentoAuctionDetails.setOnClickListener{
            postDisiscrizione(idAuction, mailUtente,token)
        }

        binding.cardPresentaOffertaFrammentoAuctionDetails.setOnClickListener{
            binding.cardBackgroundOverlayFrammentoAuctionDetails.isVisible = true
        }

        binding.bottoneOverlay1FrammentoAuctionDetails.setOnClickListener{
            val viaSpedizione = binding.editTextOverlayViaSpedizioneFrammentoAuctionDetails.text.toString()
            val cittaSpedizione = binding.editTextOverlayCittaSpedizioneFrammentoAuctionDetails.text.toString()
            val provinciaSpedizione = binding.editTextOverlayProvinciaSpedizioneFrammentoAuctionDetails.text.toString()
            val CAPSpedizione = binding.editTextOverlayCAPSpedizioneFrammentoAuctionDetails.text.toString()

            val viaFatturazione = binding.editTextOverlayViaFatturazioneFrammentoAuctionDetails.text.toString()
            val cittaFatturazione = binding.editTextOverlayCittaFatturazioneFrammentoAuctionDetails.text.toString()
            val provinciaFatturazione = binding.editTextOverlayProvinciaFatturazioneFrammentoAuctionDetails.text.toString()
            val CAPFatturazione = binding.editTextOverlayCAPFatturazioneFrammentoAuctionDetails.text.toString()

            if((viaSpedizione=="" )||( cittaSpedizione=="")||( provinciaSpedizione=="" )||(CAPSpedizione=="")||(viaFatturazione=="")||(cittaFatturazione=="")||(provinciaFatturazione=="")||(CAPFatturazione=="")){
                Toast.makeText(this.context, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
            }else {
                binding.scrollViewOverlay2FrammentoAuctionDetails.isVisible = true
                binding.textViewLabelOverlayTuaOffertaFrammentoAuctionDetails.requestFocus()
                binding.scrollViewOverlay1FrammentoAuctionDetails.isVisible = false
            }
        }
        binding.bottoneOverlay2FrammentoAuctionDetails.setOnClickListener{
            val offerta = binding.editTextOverlayTuaOffertaFrammentoAuctionDetails.text.toString()
            val numeroCarta = binding.editTextOverlayNumeroOffertaFrammentoAuctionDetails.text.toString()
            val nomeIntestatarioCarta = binding.editTextOverlayIntestatarioCartaFrammentoAuctionDetails.text.toString()
            val dataScadenzaMese = binding.editTextOverlayDataScadenzaMeseFrammentoAuctionDetails.text.toString()
            val dateScadenzaAnno = binding.editTextOverlayDataScadenzaAnnoFrammentoAuctionDetails.text.toString()
            val ccv = binding.editTextOverlayCCVFrammentoAuctionDetails.text.toString()

            if((offerta=="")||(numeroCarta=="")||(nomeIntestatarioCarta=="")||(dataScadenzaMese=="")||(dateScadenzaAnno=="")||(ccv=="")){
                Toast.makeText(this.context, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
            }else{
                postNuovaOfferta(idAuction, mailUtente,offerta.toDouble(), token)
            }
        }


        binding.imageViewBackButtonOverlayPresentaOffertaFrammentoAuctionDetails.setOnClickListener{
            if(binding.scrollViewOverlay2FrammentoAuctionDetails.isVisible == true){


                binding.editTextOverlayTuaOffertaFrammentoAuctionDetails.setText("")
                binding.editTextOverlayNumeroOffertaFrammentoAuctionDetails.setText("")
                binding.editTextOverlayIntestatarioCartaFrammentoAuctionDetails.setText("")
                binding.editTextOverlayDataScadenzaMeseFrammentoAuctionDetails.setText("")
                binding.editTextOverlayDataScadenzaAnnoFrammentoAuctionDetails.setText("")
                binding.editTextOverlayCCVFrammentoAuctionDetails.setText("")

                binding.textViewLabelOverlayTuaOffertaFrammentoAuctionDetails.requestFocus()
                binding.scrollViewOverlay2FrammentoAuctionDetails.isVisible = false
            }


            binding.editTextOverlayViaSpedizioneFrammentoAuctionDetails.setText("")
            binding.editTextOverlayCittaSpedizioneFrammentoAuctionDetails.setText("")
            binding.editTextOverlayProvinciaSpedizioneFrammentoAuctionDetails.setText("")
            binding.editTextOverlayCAPSpedizioneFrammentoAuctionDetails.setText("")

            binding.editTextOverlayViaFatturazioneFrammentoAuctionDetails.setText("")
            binding.editTextOverlayCittaFatturazioneFrammentoAuctionDetails.setText("")
            binding.editTextOverlayProvinciaFatturazioneFrammentoAuctionDetails.setText("")
            binding.editTextOverlayCAPFatturazioneFrammentoAuctionDetails.setText("")

            binding.scrollViewOverlay1FrammentoAuctionDetails.isVisible = true
            binding.textViewLabelOverlayIndirizzoSpedizioneFrammentoAuctionDetails.requestFocus()
            binding.cardBackgroundOverlayFrammentoAuctionDetails.isVisible= false
        }

        binding.textViewNomeAutoreFrammentoAuctionDetails.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, OthersProfileFragment(mailUtente, token))
            transaction?.addToBackStack(this.toString())
            transaction?.commit()
        }


        return binding.root
    }








    fun setAuction(dettagliAsta: DettagliAstaDTO, immagineProdotto: Bitmap){
        var dataScadenza: ZonedDateTime = ZonedDateTime.of(dettagliAsta.anno, dettagliAsta.mese, dettagliAsta.giorno, 0, 0, 0,0, ZoneId.of("GMT"))
        binding.imageViewImmagineAuctionFrammentoAuctionDetails.setImageBitmap(immagineProdotto)
        binding.textViewTitoloAstaFrammentoAuctionDetails.text = dettagliAsta.titolo
        Log.println(Log.INFO, "MyNetwork", dettagliAsta.ultimaOffertaTua.toString())
        Log.println(Log.INFO, "MyNetwork", dettagliAsta.statusLegame)

        if(dettagliAsta.ultimaOffertaTua){
            binding.textViewUltimaOffertaFrammentoAuctionDetails.text = "Ultima Offerta (Tu): €"+ "%,.2f".format(Locale.ITALIAN,dettagliAsta.ultimaOfferta)

        }else{
            binding.textViewUltimaOffertaFrammentoAuctionDetails.text = "Ultima Offerta: €"+ "%,.2f".format(Locale.ITALIAN,dettagliAsta.ultimaOfferta)
        }
        binding.textViewDescrizioneValueAuctionFrammentoAuctionDetails.text = dettagliAsta.descrizione
        if(dataScadenza.isBefore(ZonedDateTime.now())){
            binding.textViewTempoRimFrammentoAuctionDetails.text = "Scaduta"
            binding.textViewTempoRimFrammentoAuctionDetails.setTextColor(Color.parseColor("#4d251b"))
            binding.BottoneDisiscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.textViewDisiscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.BottoneIscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.textViewIscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.BottonePresentaOffertaFrammentoAuctionDetails.isVisible = false
            binding.textViewPresentaOffertaFrammentoAuctionDetails.isVisible = false
        }else {
            var tempoRimanente = ZonedDateTime.now().until(dataScadenza, ChronoUnit.HOURS)
            if(tempoRimanente >48) {
                binding.textViewTempoRimFrammentoAuctionDetails.text =""+(tempoRimanente/24).toString() + "g rimanenti"
            }else if(48>tempoRimanente && tempoRimanente>24){
                binding.textViewTempoRimFrammentoAuctionDetails.text ="1g rimanente"
            }else if (tempoRimanente > 0) {
                binding.textViewTempoRimFrammentoAuctionDetails.text = "" + tempoRimanente.toString() + "h rimanenti"
            } else {
                binding.textViewTempoRimFrammentoAuctionDetails.text = ">1h rimanente"
            }
        }
        if(dettagliAsta.tipoAsta =="Asta Inversa"){
            binding.cardIsInversaFrammentoAuctionDetails.isVisible = true
        }
        binding.textViewCategoriaFrammentoAuctionDetails.text = "Categoria: "+dettagliAsta.categoria
        binding.textViewNomeAutoreFrammentoAuctionDetails.text = dettagliAsta.nomeAutore

        if(dettagliAsta.emailCreatore.equals(mailUtente)){ //l'asta è dell'utente
            disabilitaIscrizione()
            disabilitaPresentaOfferta()
        }else{
            if(dettagliAsta.statusLegame=="OffertaFatta"){ //l'utente ha già presentato una offerta
                disabilitaIscrizione()
            }else{
                if(dettagliAsta.statusLegame=="Iscritto"){ //l'utente è già iscritto all'asta
                    cambiaDaIscrizioneADisiscrizione()
                }
            }
        }

    }
    fun disabilitaIscrizione(){
        binding.cardIscrizioneAstaFrammentoAuctionDetails.isVisible = false
        //binding.textViewIscrizioneAstaFrammentoAuctionDetails.isVisible = false
    }
    fun disabilitaPresentaOfferta(){
        binding.cardPresentaOffertaFrammentoAuctionDetails.isVisible=false
        //binding.textViewPresentaOffertaFrammentoAuctionDetails.isVisible = false
    }
    fun cambiaDaIscrizioneADisiscrizione(){
        binding.cardDisiscrizioneAstaFrammentoAuctionDetails.isVisible = true
        binding.cardIscrizioneAstaFrammentoAuctionDetails.isVisible = false
    }
    fun cambiaDaDisiscrizioneAIscrizione(){
        binding.cardIscrizioneAstaFrammentoAuctionDetails.isVisible = true
        binding.cardDisiscrizioneAstaFrammentoAuctionDetails.isVisible = false
    }













    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDettagliAsta(id: Int, mailUtente: String, token: String){

        lifecycleScope.async {
            val response = try{
                RetrofitInstance.api.getDettagliAsta(id, mailUtente, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return@async
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return@async
            }catch (e: Exception){
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO, "MyNetwork", "Response is successful")
                var astaRecuperata: DettagliAstaDTO = response.body()!!
                Log.println(Log.INFO, "MyNetwork", astaRecuperata.toString())


                val immagineAstaByteArrayDecoded = Base64.getDecoder().decode(astaRecuperata.immagineAsta)


                Log.println(Log.INFO, "MyNetwork", "Ho trasformato la stringa in byte Array")

                try{
                    var immagineAsta= BitmapFactory.decodeByteArray(immagineAstaByteArrayDecoded, 0, immagineAstaByteArrayDecoded.size)


                    Log.println(Log.INFO, "MyNetwork", "Ho trasformato il byte array in bitmap")
                    setAuction(astaRecuperata, immagineAsta)
                    binding.frammentoIntero.isVisible=true

                    return@async
                }catch (e: Exception){
                    Log.e("MyNetwork", e.toString())
                    Log.e("MyNetwork", e.message.toString())
                    Log.e("MyNetwork", e.cause.toString())
                }
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                activity?.finish()
                return@async
            }
        }
    }


    private fun postIscrizione(idAsta: Int, mailUtente: String, token: String){
        var offertaDTO = OffertaDTO(idAsta, mailUtente, -1.0)
        lifecycleScope.async {
            val response = try{
                RetrofitInstance.api.postIscrizione(offertaDTO, token)
            }catch (e: IOException){
                Toast.makeText(context, "Iscrizione fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return@async
            }catch (e: HttpException){
                Toast.makeText(context, "Iscrizione fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", "HttpException, unexpected response")
                return@async
            }catch (e: Exception){
                Toast.makeText(context, "Iscrizione fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO, "MyNetwork", "Response is successful")
                if(response.body()==1){
                    cambiaDaIscrizioneADisiscrizione()
                    Toast.makeText(context, "Iscrizione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Iscrizione fallita!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Iscrizione fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                activity?.finish()
                return@async
            }
        }
    }

    private fun postDisiscrizione(idAsta: Int, mailUtente: String, token: String){
        var offertaDTO = OffertaDTO(idAsta, mailUtente, -1.0)
        lifecycleScope.async {
            val response = try{
                RetrofitInstance.api.postDisiscrizione(offertaDTO, token)
            }catch (e: IOException){
                Toast.makeText(context, "Disiscrizione fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return@async
            }catch (e: HttpException){
                Toast.makeText(context, "Disiscrizione fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", "HttpException, unexpected response")
                return@async
            }catch (e: Exception){
                Toast.makeText(context, "Disiscrizione fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO, "MyNetwork", "Response is successful")
                if(response.body()==1){
                    cambiaDaDisiscrizioneAIscrizione()
                    Toast.makeText(context, "Disiscrizione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Disiscrizione fallita!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Disiscrizione fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                activity?.finish()
                return@async
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun postNuovaOfferta(idAsta: Int, mailUtente: String, offerta: Double, token: String){
        var offertaDTO = OffertaDTO(idAsta, mailUtente, offerta)
        lifecycleScope.async {
            val response = try{
                RetrofitInstance.api.postNuovaOfferta(offertaDTO, token)
            }catch (e: IOException){
                Toast.makeText(context, "Presentazione nuova offerta fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return@async
            }catch (e: HttpException){
                Toast.makeText(context, "Presentazione nuova offerta fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", "HttpException, unexpected response")
                return@async
            }catch (e: Exception){
                Toast.makeText(context, "Presentazione nuova offerta fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO, "MyNetwork", "Response is successful")
                if(response.body()==1){
                    cambiaDaDisiscrizioneAIscrizione()
                    Toast.makeText(context, "Presentazione nuova offerta avvenuta con successo!", Toast.LENGTH_SHORT).show()
                    binding.cardBackgroundOverlayFrammentoAuctionDetails.isVisible = false
                    binding.textViewLabelOverlayTuaOffertaFrammentoAuctionDetails.requestFocus()
                    binding.scrollViewOverlay2FrammentoAuctionDetails.isVisible = false

                    binding.editTextOverlayTuaOffertaFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayNumeroOffertaFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayIntestatarioCartaFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayDataScadenzaMeseFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayDataScadenzaAnnoFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayCCVFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayViaSpedizioneFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayCittaSpedizioneFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayProvinciaSpedizioneFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayCAPSpedizioneFrammentoAuctionDetails.setText("")

                    binding.editTextOverlayViaFatturazioneFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayCittaFatturazioneFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayProvinciaFatturazioneFrammentoAuctionDetails.setText("")
                    binding.editTextOverlayCAPFatturazioneFrammentoAuctionDetails.setText("")

                    binding.cardBackgroundOverlayFrammentoAuctionDetails.isVisible = false
                    binding.textViewLabelOverlayTuaOffertaFrammentoAuctionDetails.requestFocus()
                    binding.scrollViewOverlay2FrammentoAuctionDetails.isVisible = false
                    binding.scrollViewOverlay1FrammentoAuctionDetails.isVisible = true
                    binding.textViewLabelOverlayIndirizzoSpedizioneFrammentoAuctionDetails.requestFocus()
                    getDettagliAsta(idAsta, mailUtente, token)
                }
                else if(response.body()==-2){
                    Toast.makeText(context, "Offerta non valida per il tipo di asta!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Presentazione nuova offerta fallita!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Presentazione nuova offerta fallita!", Toast.LENGTH_SHORT).show()
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                activity?.finish()
                return@async
            }
        }
    }



}