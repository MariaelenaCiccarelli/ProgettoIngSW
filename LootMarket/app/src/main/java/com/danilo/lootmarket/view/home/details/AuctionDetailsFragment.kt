package com.danilo.lootmarket.view.home.details
import android.graphics.Bitmap

import android.graphics.Color
import android.os.Build
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Controller.Controller
import com.danilo.lootmarket.R
import com.danilo.lootmarket.databinding.FragmentAuctionDetailsBinding
import com.danilo.lootmarket.modelview.DettagliAstaView
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

import java.util.Locale


class AuctionDetailsFragment(private var idAuction: Int, private var mailUtente: String, private var token: String): Fragment() {

    private lateinit var binding: FragmentAuctionDetailsBinding
    private var mailCreatore = ""
    private lateinit var controller: Controller


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        binding = FragmentAuctionDetailsBinding.inflate(layoutInflater)
        binding.frammentoIntero.isVisible=false
        controller = Controller()
        lifecycleScope.async {
            var (astaRecuperata, immagineAsta) = controller.getDettagliAsta(idAuction, mailUtente, token)
            if(astaRecuperata==null || immagineAsta==null){
                activity?.finish()
            }else{
                setAuction(astaRecuperata, immagineAsta)
                binding.frammentoIntero.isVisible=true
            }
        }



        binding.cardBackButtonFrammentoAuctionDetails.setOnClickListener{
            parentFragmentManager.popBackStack()

        }


        binding.cardIscrizioneAstaFrammentoAuctionDetails.setOnClickListener{
            lifecycleScope.async {
                if(controller.postIscrizione(idAuction, mailUtente, token)){
                    Toast.makeText(context, "Iscrizione fallita!", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }else{
                    cambiaDaIscrizioneADisiscrizione()
                    Toast.makeText(context, "Iscrizione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.cardDisiscrizioneAstaFrammentoAuctionDetails.setOnClickListener{
            lifecycleScope.async {
                if(controller.postDisiscrizione(idAuction, mailUtente, token)){
                    cambiaDaDisiscrizioneAIscrizione()
                    Toast.makeText(context, "Disiscrizione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Disiscrizione fallita!", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }
            }

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
                if((dataScadenzaMese.toInt()>0.0) && (dataScadenzaMese.toInt()<13) &&(dateScadenzaAnno.toInt()>0.0) && (LocalDate.now().isBefore(LocalDate.of(("20"+dateScadenzaAnno).toInt(), dataScadenzaMese.toInt(), 1)))){
                    lifecycleScope.async {
                        var status = controller.postNuovaOfferta(idAuction, mailUtente,offerta.toDouble(), token)
                        if(status==1){
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

                            var (astaRecuperata, immagineAsta) = controller.getDettagliAsta(idAuction, mailUtente, token)
                            if(astaRecuperata==null || immagineAsta==null){
                                activity?.finish()
                            }else{
                                setAuction(astaRecuperata, immagineAsta)
                                binding.frammentoIntero.isVisible=true
                            }
                        }else{
                            if(status==-2){
                                Toast.makeText(context, "Offerta non valida per il tipo di asta!", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context, "Presentazione nuova offerta fallita!", Toast.LENGTH_SHORT).show()
                                if(status==-3) {
                                    activity?.finish()
                                }else{

                                }
                            }

                        }
                    }
                }else{
                    Toast.makeText(this.context, "Inserisci una carta valida!", Toast.LENGTH_SHORT).show()
                }
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
            transaction?.replace(R.id.frame_container, OthersProfileFragment(mailUtente, token, mailCreatore))
            transaction?.addToBackStack(this.toString())
            transaction?.commit()
        }


        return binding.root
    }





    private fun setAuction(dettagliAsta: DettagliAstaView, immagineProdotto: Bitmap){
        mailCreatore = dettagliAsta.emailCreatore

        binding.imageViewImmagineAuctionFrammentoAuctionDetails.setImageBitmap(immagineProdotto)
        binding.textViewTitoloAstaFrammentoAuctionDetails.text = dettagliAsta.titolo
        if(dettagliAsta.ultimaOffertaTua){
            binding.textViewUltimaOffertaFrammentoAuctionDetails.text = "Ultima Offerta (Tu): €"+ "%,.2f".format(Locale.ITALIAN,dettagliAsta.ultimaOfferta)
        }else{
            binding.textViewUltimaOffertaFrammentoAuctionDetails.text = "Ultima Offerta: €"+ "%,.2f".format(Locale.ITALIAN,dettagliAsta.ultimaOfferta)
        }
        binding.textViewDescrizioneValueAuctionFrammentoAuctionDetails.text = dettagliAsta.descrizione
        if(dettagliAsta.dataScadenza.isBefore(ZonedDateTime.now())){
            binding.textViewTempoRimFrammentoAuctionDetails.text = "Scaduta"
            binding.textViewTempoRimFrammentoAuctionDetails.setTextColor(Color.parseColor("#4d251b"))
            binding.BottoneDisiscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.textViewDisiscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.BottoneIscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.textViewIscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.BottonePresentaOffertaFrammentoAuctionDetails.isVisible = false
            binding.textViewPresentaOffertaFrammentoAuctionDetails.isVisible = false
        }else {
            var tempoRimanente = ZonedDateTime.now().until(dettagliAsta.dataScadenza, ChronoUnit.HOURS)
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
        if(dettagliAsta.tipoAsta =="Asta Conclusa"){
            disabilitaIscrizione()
            disabilitaPresentaOfferta()
            if(dettagliAsta.ultimaOfferta<-1){
                binding.textViewUltimaOffertaFrammentoAuctionDetails.text="Terminata senza successo"
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





    private fun disabilitaIscrizione(){
        binding.cardIscrizioneAstaFrammentoAuctionDetails.isClickable= false
        binding.cardBottoneIscrizioneAstaFrammentoAuctionDetails.isVisible = false
        binding.textViewIscrizioneAstaFrammentoAuctionDetails.isVisible = false
    }


    private fun disabilitaPresentaOfferta(){
        binding.cardPresentaOffertaFrammentoAuctionDetails.isVisible=false
    }


    private fun cambiaDaIscrizioneADisiscrizione(){
        binding.cardDisiscrizioneAstaFrammentoAuctionDetails.isVisible = true
        binding.cardIscrizioneAstaFrammentoAuctionDetails.isVisible = false
    }


    private fun cambiaDaDisiscrizioneAIscrizione(){
        binding.cardIscrizioneAstaFrammentoAuctionDetails.isVisible = true
        binding.cardDisiscrizioneAstaFrammentoAuctionDetails.isVisible = false
    }

}