package com.danilo.lootmarket.view.home.details


import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Controller.Controller
import com.danilo.lootmarket.modelview.IndirizzoView
import com.danilo.lootmarket.modelview.UserBaseView
import com.danilo.lootmarket.modelview.UserBusinessView
import com.danilo.lootmarket.R
import com.danilo.lootmarket.databinding.FragmentProfileBinding
import kotlinx.coroutines.async
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


class ProfileFragment(var mail: String, var token: String) : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var isBusiness: Boolean = false
    private lateinit var utente: UserBusinessView
    private lateinit var controller: Controller
    lateinit var galleryUri: Uri
    private var immagineCambiata:Boolean = false

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        immagineCambiata = true
        galleryUri = it!!
        try {
            binding.imageViewImmagineUtenteFrammentoProfile.setImageURI(galleryUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(layoutInflater)
        controller = Controller()
        lifecycleScope.async {
            var utenteRecuperato: UserBusinessView?
            utenteRecuperato = controller.getDatiUtentePersonali(mail, token)
            if(utenteRecuperato==null){
                activity?.finish()
            }else{
                utente = utenteRecuperato
                if(utente.ragioneSociale.isEmpty()){
                    isBusiness= false
                    setDatiBase(utente.informazioniBase)
                    disattivaCampiBusiness()
                }else{
                    isBusiness = true
                    setDatiBusiness(utente)
                    attivaCampiBusiness()
                }
            }
        }

        var spinner : Spinner = binding.spinnerNazioneFrammentoProfile
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.countries_array,
            android.R.layout.simple_spinner_item
        ).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        disattivaCampi(isBusiness)

        binding.bottoneModificaFrammentoProfile.setOnClickListener{
            attivaCampi(isBusiness)
        }


        binding.bottoneAnnullaModificheFrammentoProfile.setOnClickListener{
            disattivaCampi(isBusiness)
            setDatiBusiness(utente)
        }


        binding.bottoneSalvaModificheFrammentoProfile.setOnClickListener{
            if(isBusiness && binding.editTextNumeroAziendaleFrammentoProfile.text.toString().equals("")){
                Toast.makeText(this.context, "Fornisci un numero aziendale!", Toast.LENGTH_SHORT).show()
            }else{
                disattivaCampi(isBusiness)
                var indirizzoViewFatturazione = IndirizzoView(binding.editTextViaFatturazioneFrammentoProfile.text.toString(), binding.editTextCittaFatturazioneFrammentoProfile.text.toString(), binding.editTextProvinciaFatturazioneFrammentoProfile.text.toString(), binding.editTextCAPFatturazioneFrammentoProfile.text.toString())
                var indirizzoViewSpedizione = IndirizzoView(binding.editTextViaSpedizioneFrammentoProfile.text.toString(), binding.editTextCittaSpedizioneFrammentoProfile.text.toString(), binding.editTextProvinciaSpedizioneFrammentoProfile.text.toString(), binding.editTextCAPSpedizioneFrammentoProfile.text.toString())
                var nazione = binding.spinnerNazioneFrammentoProfile.selectedItem.toString()
                var numeroCellulare = binding.editTextNumeroCellulareFrammentoProfile.text.toString()
                var sito = binding.editTextSitowebFrammentoProfile.text.toString()
                var socialFacebook = binding.editTextSocialFacebookFrammentoProfile.text.toString()
                var socialInstagram = binding.editTextSocialInstagramFrammentoProfile.text.toString()
                var biografia = binding.editTextBiografiaFrammentoProfile.text.toString()
                var numeroAziendale = binding.editTextNumeroAziendaleFrammentoProfile.text.toString()
                var partImmagineDTO: MultipartBody.Part
                if(immagineCambiata==true){
                    val fileDir = context?.applicationContext?.filesDir
                    val imageFile = File(fileDir, "immagineProfilo.png")
                    val inputStream = context?.contentResolver?.openInputStream(galleryUri)
                    val outputStream = FileOutputStream(imageFile)
                    inputStream!!.copyTo((outputStream))
                    val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                    partImmagineDTO = MultipartBody.Part.createFormData("immagineProfiloDTO", imageFile.name, requestBody)
                    immagineCambiata=false
                }
                else{
                    val fileDir = context?.applicationContext?.filesDir
                    val imageFile = File(fileDir, "immagineProfilo.png")
                    try{
                        var out= FileOutputStream(imageFile)
                        utente.informazioniBase.immagine.compress(Bitmap.CompressFormat.PNG, 90, out)
                        out.flush()
                        out.close()
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                    val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                    partImmagineDTO = MultipartBody.Part.createFormData("immagineProfiloDTO", imageFile.name, requestBody)
                }
                lifecycleScope.async {
                    if(controller.postModificaUtente(partImmagineDTO, utente, nazione, numeroCellulare, indirizzoViewSpedizione, indirizzoViewFatturazione, sito, socialFacebook, socialInstagram, biografia, numeroAziendale, token)){

                        var utenteRecuperato: UserBusinessView?
                        utenteRecuperato = controller.getDatiUtentePersonali(mail, token)
                        if(utenteRecuperato==null){
                            activity?.finish()
                        }else{
                            utente = utenteRecuperato as UserBusinessView
                            if(utente.ragioneSociale.isEmpty()){
                                isBusiness= false
                                setDatiBase(utente.informazioniBase)
                                disattivaCampiBusiness()
                            }else{
                                isBusiness = true
                                setDatiBusiness(utente)
                                attivaCampiBusiness()
                            }
                        }
                    }else{
                        annullaPassaBusiness()
                        Toast.makeText(context, "Modifica dati utente fallita", Toast.LENGTH_SHORT).show()
                        activity?.finish()
                    }
                }
            }
        }

        //fa apparire l'overlay per l'inserimento dei dati business
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
                var indirizzoViewFatturazione = IndirizzoView(binding.editTextViaFatturazioneFrammentoProfile.text.toString(), binding.editTextCittaFatturazioneFrammentoProfile.text.toString(), binding.editTextProvinciaFatturazioneFrammentoProfile.text.toString(), binding.editTextCAPFatturazioneFrammentoProfile.text.toString())
                var indirizzoViewSpedizione = IndirizzoView(binding.editTextViaSpedizioneFrammentoProfile.text.toString(), binding.editTextCittaSpedizioneFrammentoProfile.text.toString(), binding.editTextProvinciaSpedizioneFrammentoProfile.text.toString(), binding.editTextCAPSpedizioneFrammentoProfile.text.toString())
                var nazione = binding.spinnerNazioneFrammentoProfile.selectedItem.toString()
                var numeroCellulare = binding.editTextNumeroCellulareFrammentoProfile.text.toString()
                var sito = binding.editTextSitowebFrammentoProfile.text.toString()
                var socialFacebook = binding.editTextSocialFacebookFrammentoProfile.text.toString()
                var socialInstagram = binding.editTextSocialInstagramFrammentoProfile.text.toString()
                var biografia = binding.editTextBiografiaFrammentoProfile.text.toString()
                var numeroAziendale = binding.editTextNumeroAziendaleFrammentoProfile.text.toString()
                lifecycleScope.async {
                    if(controller.postUpgradeUtente(utente, nazione, numeroCellulare, indirizzoViewSpedizione, indirizzoViewFatturazione, sito, socialFacebook, socialInstagram, biografia, numeroAziendale, token)){
                        var utenteBusiness = UserBusinessView(utente.informazioniBase, ragioneSociale, partitaIva, numeroAziendale)
                        isBusiness = true
                        binding.textViewValueRagioneSocialeFrammentoProfile.setText(utenteBusiness.ragioneSociale)
                        binding.textViewValuePartivaIvaFrammentoProfile.setText(utenteBusiness.partitaIva)
                        binding.editTextNumeroAziendaleFrammentoProfile.setText(utenteBusiness.numeroAziendale)
                        attivaCampiBusiness()
                    }else{
                        annullaPassaBusiness()
                        Toast.makeText(context, "Passaggio a Business fallito", Toast.LENGTH_SHORT).show()
                        activity?.finish()
                    }
                }
            }
        }

        //annulla passa a business
        binding.imageViewBackButtonOverlayFrammentoProfile.setOnClickListener{
            annullaPassaBusiness()
        }

        binding.cardBottoneCambiaImmagineFrammentoProfile.setOnClickListener{
            galleryLauncher.launch("image/*")
        }

        return binding.root
    }


        //dato un utente base, compila i suoi campi nella pagina
        private fun setDatiBase(utente: UserBaseView){
            //Settaggio campi
            binding.imageViewImmagineUtenteFrammentoProfile.setImageBitmap(utente.immagine)
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

            binding.editTextBiografiaFrammentoProfile.setText(utente.biografia)

        }


    //dato un utente compila tutti i suoi campi, compresi quelli business
    private fun setDatiBusiness(utenteBusiness: UserBusinessView){
        setDatiBase(utenteBusiness.informazioniBase)
        binding.textViewValueRagioneSocialeFrammentoProfile.setText(utenteBusiness.ragioneSociale)
        binding.textViewValuePartivaIvaFrammentoProfile.setText(utenteBusiness.partitaIva)
        binding.editTextNumeroAziendaleFrammentoProfile.setText(utenteBusiness.numeroAziendale)
    }

    //Attiva la modifica dei campi, inclusi quelli Business se l'utente è Business
    private fun attivaCampi(isBusiness: Boolean){

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

    //Disattiva la modifica dei campi, inclusi quelli Business nel caso l'utente lo sia
    private fun disattivaCampi(isBusiness: Boolean){
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


    //Rende invisibili i campi Business
    private fun disattivaCampiBusiness(){
        binding.textViewLabelInfoBusinessFrammentoProfile.isVisible = false
        binding.textViewLabelRagioneSocialeFrammentoProfile.isVisible = false
        binding.textViewValueRagioneSocialeFrammentoProfile.isVisible = false
        binding.textViewLabelPartitaIvaFrammentoProfile.isVisible = false
        binding.textViewValuePartivaIvaFrammentoProfile.isVisible = false
        binding.textViewLabelNumeroAziendaleFrammentoProfile.isVisible = false
        binding.cardEditTextNumeroAziendaleFrammentoProfile.isVisible = false
        binding.cardOverlayFrammentoProfile.isVisible = false
    }

    //Rende visibile i campi Business
    private fun attivaCampiBusiness(){
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

    private fun annullaPassaBusiness(){
        binding.cardOverlayFrammentoProfile.isVisible = false
        binding.cardBackgroundOverlayFrammentoProfile.isVisible = false
        binding.cardBottoneModificaFrammentoProfile.isVisible = true

        binding.editTextOverlayRagioneSocialeFrammentoProfile.setText("")
        binding.editTextOverlayNumeroAziendaleFrammentoProfile.setText("")
        binding.editTextOverlayPartivaIvaFrammentoProfile.setText("")
    }



}