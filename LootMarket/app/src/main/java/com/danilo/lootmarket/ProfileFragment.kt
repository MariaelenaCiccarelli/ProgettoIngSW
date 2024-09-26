package com.danilo.lootmarket


import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.danilo.lootmarket.databinding.FragmentProfileBinding
import com.danilo.lootmarket.databinding.FragmentSearchBinding
import java.time.LocalDate

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try {
            binding.imageViewImmagineUtenteFrammentoProfile.setImageURI(galleryUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var maria = UserBase(1, (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable), "Mariaelena", "CC233118908", "mariaelena@gmail.com", LocalDate.of(2000, 6, 21), "Italia", "34683726860", indirizzoSpedizione = Indirizzo("Via Claudio 25", "Napoli", "NA", "80125"), indirizzoFatturazione = Indirizzo("Via Claudio 25", "Napoli", "NA", "80125"), "www.mariaelena.com", "mariaelena", "mariaelena", "Ciao a tutti!")


        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        var spinner : Spinner = binding.spinnerNazioneFrammentoProfile
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.countries_array,
            android.R.layout.simple_spinner_item
        ).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        var isBusiness: Boolean = false

        //if utente base
        setDatiBase(maria)
        disattivaCampiBusiness()
        //else

        disattivaCampi(isBusiness)


        binding.bottoneModificaFrammentoProfile.setOnClickListener{

            attivaCampi(isBusiness)

        }

        binding.bottoneAnnullaModificheFrammentoProfile.setOnClickListener{
            disattivaCampi(isBusiness)
            setDatiBase(maria)

        }
        binding.bottoneSalvaModificheFrammentoProfile.setOnClickListener{
            disattivaCampi(isBusiness)
            //invia modiche al backend
            //carica utente dal backend
            //setDatiBase(maria)
        }

        binding.bottonePassaABusinessFrammentoProfile.setOnClickListener{
            binding.cardBackgroundOverlayFrammentoProfile.isVisible = true
            binding.cardOverlayFrammentoProfile.isVisible = true
            binding.cardOverlayFrammentoProfile.isClickable = true
            binding.cardBottoneModificaFrammentoProfile.isVisible = false

        }

        binding.bottoneOverlayFrammentoProfile.setOnClickListener{
            val ragioneSociale = binding.editTextOverlayRagioneSocialeFrammentoProfile.text.toString()
            val partitaIva = binding.editTextOverlayPartivaIvaFrammentoProfile.text.toString()
            val numeroAziendale = binding.editTextOverlayNumeroAziendaleFrammentoProfile.text.toString()

            if(ragioneSociale ==""||partitaIva==""||numeroAziendale==""){
                Toast.makeText(this.context, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
            }else {
                var mariaBusiness = UserBusiness(maria, ragioneSociale, partitaIva, numeroAziendale)
                isBusiness = true
                binding.textViewValueRagioneSocialeFrammentoProfile.setText(mariaBusiness.ragioneSociale)
                binding.textViewValuePartivaIvaFrammentoProfile.setText(mariaBusiness.partitaIva)
                binding.editTextNumeroAziendaleFrammentoProfile.setText(mariaBusiness.numeroAziendale)
                attivaCampiBusiness()
            }
        }
        binding.imageViewBackButtonOverlayFrammentoProfile.setOnClickListener{
            binding.cardOverlayFrammentoProfile.isVisible = false
            binding.cardBackgroundOverlayFrammentoProfile.isVisible = false
            binding.cardBottoneModificaFrammentoProfile.isVisible = true

            binding.editTextOverlayRagioneSocialeFrammentoProfile.setText("")
            binding.editTextOverlayNumeroAziendaleFrammentoProfile.setText("")
            binding.editTextOverlayPartivaIvaFrammentoProfile.setText("")

        }
        binding.cardBottoneCambiaImmagineFrammentoProfile.setOnClickListener{
            galleryLauncher.launch("image/*")
        }

        return binding.root
    }













        fun setDatiBase(utente: UserBase){
            //Settaggio campi
            binding.imageViewImmagineUtenteFrammentoProfile.setImageDrawable(utente.immagine)
            binding.textViewNomeUtenteFrammentoProfile.text = utente.nome
            binding.textViewValueCodiceFiscaleFrammentoProfile.text = utente.codiceFiscale
            binding.textViewValueMailFrammentoProfile.text = utente.mail
            binding.textViewValueDataNascitaFrammentoProfile.text = ""+utente.dataDiNascita.dayOfMonth+"/"+utente.dataDiNascita.monthValue+"/"+utente.dataDiNascita.year
            binding.spinnerNazioneFrammentoProfile.setSelection(resources.getStringArray(R.array.countries_array).indexOf(utente.nazione))
            binding.editTextNumeroCellulareFrammentoProfile.setText(utente.numeroCellulare)

            binding.editTextViaSpedizioneFrammentoProfile.setText(utente.indirizzoSpedizione.via)
            binding.editTextCittaSpedizioneFrammentoProfile.setText(utente.indirizzoSpedizione.citta)
            binding.editTextProvinciaSpedizioneFrammentoProfile.setText(utente.indirizzoSpedizione.provincia)
            binding.editTextCAPSpedizioneFrammentoProfile.setText(utente.indirizzoSpedizione.cap)

            binding.editTextViaFatturazioneFrammentoProfile.setText(utente.indirizzoFatturazione.via)
            binding.editTextCittaFatturazioneFrammentoProfile.setText(utente.indirizzoFatturazione.citta)
            binding.editTextProvinciaFatturazioneFrammentoProfile.setText(utente.indirizzoFatturazione.provincia)
            binding.editTextCAPFatturazioneFrammentoProfile.setText(utente.indirizzoFatturazione.cap)

            binding.editTextSitowebFrammentoProfile.setText(utente.sito)
            binding.editTextSocialFacebookFrammentoProfile.setText(utente.socialFacebook)
            binding.editTextSocialInstagramFrammentoProfile.setText(utente.socialInstagram)

            binding.editTextBiografiaFrammentoProfile.setText(utente.Biografia)
        }

    fun attivaCampi(isBusiness: Boolean){

        binding.cardBottoneCambiaImmagineFrammentoProfile.isVisible= true
        binding.spinnerNazioneFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.spinnerNazioneFrammentoProfile.isEnabled= true

        binding.editTextNumeroCellulareFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextNumeroCellulareFrammentoProfile.isEnabled= true

        binding.editTextViaSpedizioneFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextViaSpedizioneFrammentoProfile.isEnabled=true
        binding.editTextCittaSpedizioneFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextCittaSpedizioneFrammentoProfile.isEnabled = true
        binding.editTextProvinciaSpedizioneFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextProvinciaSpedizioneFrammentoProfile.isEnabled = true
        binding.editTextCAPSpedizioneFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextCAPSpedizioneFrammentoProfile.isEnabled = true

        binding.editTextViaFatturazioneFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextViaFatturazioneFrammentoProfile.isEnabled=true
        binding.editTextCittaFatturazioneFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextCittaFatturazioneFrammentoProfile.isEnabled = true
        binding.editTextProvinciaFatturazioneFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextProvinciaFatturazioneFrammentoProfile.isEnabled = true
        binding.editTextCAPFatturazioneFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextCAPFatturazioneFrammentoProfile.isEnabled = true

        binding.editTextSitowebFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextSitowebFrammentoProfile.isEnabled = true

        binding.editTextSocialFacebookFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextSocialFacebookFrammentoProfile.isEnabled = true

        binding.editTextSocialInstagramFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextSocialInstagramFrammentoProfile.isEnabled = true

        binding.editTextBiografiaFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
        binding.editTextBiografiaFrammentoProfile.isEnabled = true

        binding.cardBottoneSalvaModificheFrammentoProfile.isVisible = true
        binding.cardBottoneAnnullaModificheFrammentoProfile.isVisible = true
        binding.cardBottoneModificaFrammentoProfile.isVisible = false

        if(isBusiness){
            binding.editTextNumeroAziendaleFrammentoProfile.setBackgroundColor(Color.parseColor("#EBCA71"))
            binding.editTextNumeroAziendaleFrammentoProfile.isEnabled = true

        }else{
            binding.bottonePassaABusinessFrammentoProfile.isVisible = false
        }


    }

    fun disattivaCampi(isBusiness: Boolean){
        binding.spinnerNazioneFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.spinnerNazioneFrammentoProfile.isEnabled= false
        binding.cardBottoneCambiaImmagineFrammentoProfile.isVisible= false

        binding.editTextNumeroCellulareFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextNumeroCellulareFrammentoProfile.isEnabled= false

        binding.editTextViaSpedizioneFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextViaSpedizioneFrammentoProfile.isEnabled=false
        binding.editTextCittaSpedizioneFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextCittaSpedizioneFrammentoProfile.isEnabled = false
        binding.editTextProvinciaSpedizioneFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextProvinciaSpedizioneFrammentoProfile.isEnabled = false
        binding.editTextCAPSpedizioneFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextCAPSpedizioneFrammentoProfile.isEnabled = false

        binding.editTextViaFatturazioneFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextViaFatturazioneFrammentoProfile.isEnabled=false
        binding.editTextCittaFatturazioneFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextCittaFatturazioneFrammentoProfile.isEnabled = false
        binding.editTextProvinciaFatturazioneFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextProvinciaFatturazioneFrammentoProfile.isEnabled = false
        binding.editTextCAPFatturazioneFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextCAPFatturazioneFrammentoProfile.isEnabled = false

        binding.editTextSitowebFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextSitowebFrammentoProfile.isEnabled = false

        binding.editTextSocialFacebookFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextSocialFacebookFrammentoProfile.isEnabled = false

        binding.editTextSocialInstagramFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextSocialInstagramFrammentoProfile.isEnabled = false

        binding.editTextBiografiaFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
        binding.editTextBiografiaFrammentoProfile.isEnabled = false

        binding.cardBottoneAnnullaModificheFrammentoProfile.isVisible= false
        binding.cardBottoneSalvaModificheFrammentoProfile.isVisible= false
        binding.cardBottoneModificaFrammentoProfile.isVisible = true

        if(isBusiness){
            binding.editTextNumeroAziendaleFrammentoProfile.setBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.editTextNumeroAziendaleFrammentoProfile.isEnabled = false

        }else{
            binding.bottonePassaABusinessFrammentoProfile.isVisible = true
        }
    }

    fun disattivaCampiBusiness(){
        binding.textViewLabelInfoBusinessFrammentoProfile.isVisible = false
        binding.textViewLabelRagioneSocialeFrammentoProfile.isVisible = false
        binding.textViewValueRagioneSocialeFrammentoProfile.isVisible = false
        binding.textViewLabelPartitaIvaFrammentoProfile.isVisible = false
        binding.textViewValuePartivaIvaFrammentoProfile.isVisible = false
        binding.textViewLabelNumeroAziendaleFrammentoProfile.isVisible = false
        binding.cardEditTextNumeroAziendaleFrammentoProfile.isVisible = false
        binding.cardOverlayFrammentoProfile.isVisible = false
    }

    fun attivaCampiBusiness(){
        binding.textViewLabelInfoBusinessFrammentoProfile.isVisible = true
        binding.textViewLabelRagioneSocialeFrammentoProfile.isVisible = true
        binding.textViewValueRagioneSocialeFrammentoProfile.isVisible = true
        binding.textViewLabelPartitaIvaFrammentoProfile.isVisible = true
        binding.textViewValuePartivaIvaFrammentoProfile.isVisible = true
        binding.textViewLabelNumeroAziendaleFrammentoProfile.isVisible = true
        binding.cardEditTextNumeroAziendaleFrammentoProfile.isVisible = true
        binding.cardBottonePassaABusinessFrammentoProfile.isVisible = false
        binding.cardOverlayFrammentoProfile.isVisible = false
        binding.cardBackgroundOverlayFrammentoProfile.isVisible = false
        binding.cardBottoneModificaFrammentoProfile.isVisible = true
    }
}