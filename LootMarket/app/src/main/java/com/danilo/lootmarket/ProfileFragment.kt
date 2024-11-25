package com.danilo.lootmarket


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.danilo.lootmarket.Network.RetrofitInstance
import com.danilo.lootmarket.Network.dto.UtenteDTO
import com.danilo.lootmarket.databinding.FragmentProfileBinding
import kotlinx.coroutines.async
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.Base64


class ProfileFragment(var mail: String, var token: String) : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var isBusiness: Boolean = false
    private lateinit var utente: UserBusiness
    lateinit var galleryUri: Uri
    var immagineCambiata:Boolean = false

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
    ): View? {

        getDatiUtentePersonali(mail)
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

        disattivaCampi(isBusiness)



        binding.bottoneModificaFrammentoProfile.setOnClickListener{
            attivaCampi(isBusiness)
        }

        binding.bottoneAnnullaModificheFrammentoProfile.setOnClickListener{
            disattivaCampi(isBusiness)
            setDatiBusiness(utente)

        }

        binding.bottoneSalvaModificheFrammentoProfile.setOnClickListener{
            disattivaCampi(isBusiness)
            var indirizzoFatturazione = Indirizzo(binding.editTextViaFatturazioneFrammentoProfile.text.toString(), binding.editTextCittaFatturazioneFrammentoProfile.text.toString(), binding.editTextProvinciaFatturazioneFrammentoProfile.text.toString(), binding.editTextCAPFatturazioneFrammentoProfile.text.toString())
            var indirizzoSpedizione = Indirizzo(binding.editTextViaSpedizioneFrammentoProfile.text.toString(), binding.editTextCittaSpedizioneFrammentoProfile.text.toString(), binding.editTextProvinciaSpedizioneFrammentoProfile.text.toString(), binding.editTextCAPSpedizioneFrammentoProfile.text.toString())


            var utenteDTO = UtenteDTO(utente.informazioniBase.nome, utente.informazioniBase.codiceFiscale, utente.informazioniBase.mail, utente.informazioniBase.dataDiNascita.year, utente.informazioniBase.dataDiNascita.monthValue, utente.informazioniBase.dataDiNascita.dayOfMonth, binding.spinnerNazioneFrammentoProfile.selectedItem.toString(), binding.editTextNumeroCellulareFrammentoProfile.text.toString(),indirizzoSpedizione, indirizzoFatturazione, binding.editTextSitowebFrammentoProfile.text.toString(), binding.editTextSocialFacebookFrammentoProfile.text.toString(), binding.editTextSocialInstagramFrammentoProfile.text.toString(), binding.editTextBiografiaFrammentoProfile.text.toString(), utente.ragioneSociale, utente.partitaIva, binding.editTextNumeroAziendaleFrammentoProfile.text.toString(),"immagineInMultiPart")
            //var immagineProfilo = binding.imageViewImmagineUtenteFrammentoProfile.drawable
            var partImmagineDTO: MultipartBody.Part
            if(immagineCambiata==true){
                val fileDir = context?.applicationContext?.filesDir
                val imageFile = File(fileDir, "immagineProfilo.png")

                Log.println(Log.INFO, "MyNetwork", "Ho creato imageFile")
                val inputStream = context?.contentResolver?.openInputStream(galleryUri)
                val outputStream = FileOutputStream(imageFile)


                inputStream!!.copyTo((outputStream))
                Log.println(Log.INFO, "MyNetwork", "Ho finito con gli stream")

                val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                partImmagineDTO = MultipartBody.Part.createFormData("immagineProfiloDTO", imageFile.name, requestBody)

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




                Log.println(Log.INFO, "MyNetwork", "Ho finito con gli stream")

                val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                partImmagineDTO = MultipartBody.Part.createFormData("immagineProfiloDTO", imageFile.name, requestBody)

            }



            postModificaUtente(partImmagineDTO, utenteDTO)
        }

        //fa apparire l'overlay per l'inserimento dei dati business
        binding.bottonePassaABusinessFrammentoProfile.setOnClickListener{
            binding.cardBackgroundOverlayFrammentoProfile.isVisible = true
            binding.cardOverlayFrammentoProfile.isVisible = true
            binding.cardOverlayFrammentoProfile.isClickable = true
            binding.cardBottoneModificaFrammentoProfile.isVisible = false

        }

        //***
        binding.bottoneOverlayFrammentoProfile.setOnClickListener{
            val ragioneSociale = binding.editTextOverlayRagioneSocialeFrammentoProfile.text.toString()
            val partitaIva = binding.editTextOverlayPartivaIvaFrammentoProfile.text.toString()
            val numeroAziendale = binding.editTextOverlayNumeroAziendaleFrammentoProfile.text.toString()

            if(ragioneSociale ==""||partitaIva==""||numeroAziendale==""){
                Toast.makeText(this.context, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
            }else {
                var indirizzoFatturazione = Indirizzo(binding.editTextViaFatturazioneFrammentoProfile.text.toString(), binding.editTextCittaFatturazioneFrammentoProfile.text.toString(), binding.editTextProvinciaFatturazioneFrammentoProfile.text.toString(), binding.editTextCAPFatturazioneFrammentoProfile.text.toString())
                var indirizzoSpedizione = Indirizzo(binding.editTextViaSpedizioneFrammentoProfile.text.toString(), binding.editTextCittaSpedizioneFrammentoProfile.text.toString(), binding.editTextProvinciaSpedizioneFrammentoProfile.text.toString(), binding.editTextCAPSpedizioneFrammentoProfile.text.toString())
                var array = byteArrayOf(0x48, 101, 108, 108, 111)
                var utenteDTO = UtenteDTO(utente.informazioniBase.nome, utente.informazioniBase.codiceFiscale, utente.informazioniBase.mail, utente.informazioniBase.dataDiNascita.year, utente.informazioniBase.dataDiNascita.monthValue, utente.informazioniBase.dataDiNascita.dayOfMonth, binding.spinnerNazioneFrammentoProfile.selectedItem.toString(), binding.editTextNumeroCellulareFrammentoProfile.text.toString(),indirizzoSpedizione, indirizzoFatturazione, binding.editTextSitowebFrammentoProfile.text.toString(), binding.editTextSocialFacebookFrammentoProfile.text.toString(), binding.editTextSocialInstagramFrammentoProfile.text.toString(), binding.editTextBiografiaFrammentoProfile.text.toString(), ragioneSociale, partitaIva, numeroAziendale, "miao")
                postUpgradeUtente(utenteDTO)
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
        fun setDatiBase(utente: UserBase){
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
    fun setDatiBusiness(utenteBusiness: UserBusiness){
        setDatiBase(utenteBusiness.informazioniBase)
        binding.textViewValueRagioneSocialeFrammentoProfile.setText(utenteBusiness.ragioneSociale)
        binding.textViewValuePartivaIvaFrammentoProfile.setText(utenteBusiness.partitaIva)
        binding.editTextNumeroAziendaleFrammentoProfile.setText(utenteBusiness.numeroAziendale)

    }

    //Attiva la modifica dei campi, inclusi quelli Business se l'utente è Business
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

    //Disattiva la modifica dei campi, inclusi quelli Business nel caso l'utente lo sia
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

    //Rende invisibili i campi Business
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

    //Rende visibile i campi Business
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

    fun annullaPassaBusiness(){
        binding.cardOverlayFrammentoProfile.isVisible = false
        binding.cardBackgroundOverlayFrammentoProfile.isVisible = false
        binding.cardBottoneModificaFrammentoProfile.isVisible = true

        binding.editTextOverlayRagioneSocialeFrammentoProfile.setText("")
        binding.editTextOverlayNumeroAziendaleFrammentoProfile.setText("")
        binding.editTextOverlayPartivaIvaFrammentoProfile.setText("")
    }


    //Carica i dati personali dell'utente dal backend
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDatiUtentePersonali(mailUtente: String){

        lifecycleScope.async {
            val response = try{
                RetrofitInstance.api.getDatiUtentePersonali(mailUtente, token)
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
                var utenteRecuperato: UtenteDTO = response.body()!!
                Log.println(Log.INFO, "MyNetwork", utenteRecuperato.toString())

                var dataNascita = LocalDate.of(utenteRecuperato.dataDiNascitaAnno, utenteRecuperato.dataDiNascitaMese, utenteRecuperato.dataDiNascitaGiorno)
                val immagineProfiloByteArrayDecoded = Base64.getDecoder().decode(utenteRecuperato.immagineProfilo)
                Log.println(Log.INFO, "MyNetwork", immagineProfiloByteArrayDecoded.toString())

                Log.println(Log.INFO, "MyNetwork", "Ho trasformato la stringa in byte Array")

                try{
                    var immagineProfilo= BitmapFactory.decodeByteArray(immagineProfiloByteArrayDecoded, 0, immagineProfiloByteArrayDecoded.size)


                Log.println(Log.INFO, "MyNetwork", "Ho trasformato il byte array in bitmap")

                    Log.println(Log.INFO, "MyNetwork", utenteRecuperato.nome)
                    Log.println(Log.INFO, "MyNetwork", immagineProfilo.toString())
                var utenteBase = UserBase(immagineProfilo, utenteRecuperato.nome, utenteRecuperato.codiceFiscale, utenteRecuperato.mail, dataNascita, utenteRecuperato.nazione, utenteRecuperato.numeroCellulare, utenteRecuperato.indirizzoSpedizione, utenteRecuperato.indirizzoFatturazione, utenteRecuperato.sito, utenteRecuperato.socialFacebook, utenteRecuperato.socialInstagram, utenteRecuperato.biografia)
                Log.println(Log.INFO, "MyNetwork", utenteBase.toString())
                if(!utenteRecuperato.ragioneSociale.equals("")){
                    Log.println(Log.INFO, "MyNetwork", "utente business recuperato!")
                    isBusiness = true
                    var utenteBusiness = UserBusiness(utenteBase, utenteRecuperato.ragioneSociale, utenteRecuperato.partitaIva, utenteRecuperato.numeroAziendale)
                    //Log.println(Log.INFO, "MyNetwork", utenteBusiness.toString())
                    setDatiBusiness(utenteBusiness)
                    attivaCampiBusiness()
                    utente = utenteBusiness
                }else{
                    Log.println(Log.INFO, "MyNetwork", "utente base recuperato!")
                    isBusiness= false
                    setDatiBase(utenteBase)
                    disattivaCampiBusiness()
                    utente = UserBusiness(utenteBase, "", "", "")
                }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun postModificaUtente(partImmagine: MultipartBody.Part, utenteDTO: UtenteDTO){

        lifecycleScope.async {

            val response = try{
                RetrofitInstance.api.postModificaUtente(partImmagine, utenteDTO, token)
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
                Log.println(Log.INFO,"MyNetwork", "Response is successful")
                getDatiUtentePersonali(mail)
                return@async
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                    activity?.finish()
                }
                if(response.code().toString().equals("500")){
                    Log.e("MyNetwork", response.code().toString()+" Modifica utente fallita!")
                    annullaPassaBusiness()
                    Toast.makeText(context, "Modifica dati utente fallita", Toast.LENGTH_SHORT).show()
                }
                return@async
            }
        }
    }

    private fun postUpgradeUtente(utenteDTO: UtenteDTO){

        lifecycleScope.async {

            val response = try{
                RetrofitInstance.api.postUpgradeUtente(utenteDTO, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                Log.e("MyNetwork", e.toString())

                annullaPassaBusiness()
                return@async
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                annullaPassaBusiness()
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO,"MyNetwork", "Response is successful")
                var utenteBusiness = UserBusiness(utente.informazioniBase, utenteDTO.ragioneSociale, utenteDTO.partitaIva, utenteDTO.numeroAziendale)
                isBusiness = true
                binding.textViewValueRagioneSocialeFrammentoProfile.setText(utenteBusiness.ragioneSociale)
                binding.textViewValuePartivaIvaFrammentoProfile.setText(utenteBusiness.partitaIva)
                binding.editTextNumeroAziendaleFrammentoProfile.setText(utenteBusiness.numeroAziendale)
                attivaCampiBusiness()
                return@async
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                    activity?.finish()
                }
                if(response.code().toString().equals("500")){
                    Log.e("MyNetwork", response.code().toString()+" Upgrade utente fallito!")
                    annullaPassaBusiness()
                    Toast.makeText(context, "Passaggio a Business fallito", Toast.LENGTH_SHORT).show()
                }

                return@async
            }
        }
    }

}