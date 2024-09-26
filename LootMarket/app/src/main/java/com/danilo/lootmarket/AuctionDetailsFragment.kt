package com.danilo.lootmarket
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import com.danilo.lootmarket.databinding.FragmentAuctionDetailsBinding
import com.danilo.lootmarket.databinding.FragmentHomeBinding
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale


class AuctionDetailsFragment(private var idAuction: Int): Fragment() {

    private lateinit var binding: FragmentAuctionDetailsBinding
    //private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment
        var auction = Auction(10,
            "Statuetta di Wargreymon",
            150000.90,
            ZonedDateTime.now().plusDays(10),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable),
            "Lorem ipsum odor amet, consectetuer adipiscing elit. Tellus tristique integer vestibulum, etiam ligula ipsum nisi. Fermentum mus nisl eleifend aliquet ipsum consequat? Ut iaculis orci efficitur gravida dui nec. Vehicula lectus adipiscing nec blandit iaculis tincidunt adipiscing, lobortis fusce. Sagittis aliquet ante aliquam aliquet tellus est. Fermentum est varius; sagittis nisl condimentum nibh. Sem placerat metus sem parturient primis cras duis bibendum. Mus tortor potenti vitae vitae sit. Non nascetur volutpat eget quis felis ante pharetra; primis mollis. Suspendisse leo fames eleifend dui vel velit rhoncus. Faucibus neque conubia id; tellus feugiat metus. Sollicitudin et penatibus luctus; congue volutpat metus convallis. Ultricies morbi placerat metus penatibus natoque sociosqu elementum turpis. Sollicitudin suscipit a egestas et vestibulum. Tempor fringilla non convallis commodo hac molestie donec viverra.",
            "Action Figures",
            "Asta inversa")




        binding = FragmentAuctionDetailsBinding.inflate(layoutInflater)
        setAuction(auction)
        //val view = inflater.inflate(R.layout.fragment_home, container, false)


        binding.cardBackButtonFrammentoAuctionDetails.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        binding.cardIscrizioneAstaFrammentoAuctionDetails.setOnClickListener{
            //codice di iscrizione
            binding.cardDisiscrizioneAstaFrammentoAuctionDetails.isVisible = true
            binding.cardIscrizioneAstaFrammentoAuctionDetails.isVisible = false
        }

        binding.cardDisiscrizioneAstaFrammentoAuctionDetails.setOnClickListener{
            //codice di disiscrizione
            binding.cardIscrizioneAstaFrammentoAuctionDetails.isVisible = true
            binding.cardDisiscrizioneAstaFrammentoAuctionDetails.isVisible = false
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
            transaction?.replace(R.id.frame_container, OthersProfileFragment())
            transaction?.addToBackStack(this.toString())
            transaction?.commit()
        }


        return binding.root
    }

    fun setAuction(auction: Auction){
        var isTua: Boolean = true
        binding.imageViewImmagineAuctionFrammentoAuctionDetails.setImageDrawable(auction.immagineProdotto)
        binding.textViewTitoloAstaFrammentoAuctionDetails.text = auction.titoloAsta
        if(isTua){
            binding.textViewUltimaOffertaFrammentoAuctionDetails.text = "Ultima Offerta (Tu): €"+ "%,.2f".format(Locale.ITALIAN,auction.ultimaOfferta)
        }else{
            binding.textViewUltimaOffertaFrammentoAuctionDetails.text = "Ultima Offerta: €"+ "%,.2f".format(Locale.ITALIAN,auction.ultimaOfferta)
        }
        binding.textViewDescrizioneValueAuctionFrammentoAuctionDetails.text = auction.testoDescrizione
        if(auction.dataScadenza.isBefore(ZonedDateTime.now())){
            binding.textViewTempoRimFrammentoAuctionDetails.text = "Scaduta"
            binding.textViewTempoRimFrammentoAuctionDetails.setTextColor(Color.parseColor("#4d251b"))
            binding.BottoneDisiscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.textViewDisiscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.BottoneIscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.textViewIscrizioneAstaFrammentoAuctionDetails.isVisible = false
            binding.BottonePresentaOffertaFrammentoAuctionDetails.isVisible = false
            binding.textViewPresentaOffertaFrammentoAuctionDetails.isVisible = false
        }else {
            var tempoRimanente = ZonedDateTime.now().until(auction.dataScadenza, ChronoUnit.HOURS)
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
        if(auction.tipoAsta =="Asta inversa"){
            binding.cardIsInversaFrammentoAuctionDetails.isVisible = true
        }
        binding.textViewCategoriaFrammentoAuctionDetails.text = "Categoria: "+auction.Categoria
    }

}