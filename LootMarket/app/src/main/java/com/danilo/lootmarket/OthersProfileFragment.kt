package com.danilo.lootmarket
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.network.RetrofitInstance
import com.danilo.lootmarket.network.dto.AstaDTO
import com.danilo.lootmarket.network.dto.UtenteDTO
import com.danilo.lootmarket.databinding.FragmentOthersProfileBinding
import kotlinx.coroutines.async
import okio.IOException
import retrofit2.HttpException
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Base64


class OthersProfileFragment(private var mailUtente: String, private var token: String, private var mailUtenteTerzo: String): Fragment() {

    private lateinit var binding: FragmentOthersProfileBinding
    private lateinit var utente: UserBusiness
    private var isBusiness = false
    private lateinit var arrayAsteUtente: List<AuctionViewHistory>
    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View{

        arrayAsteUtente = listOf()
        getDatiUtenteTerzi(mailUtenteTerzo)
        auctionsLiveAdapter = AuctionsLiveAdapter(arrayAsteUtente, false)


        binding = FragmentOthersProfileBinding.inflate(layoutInflater)

        binding.RecyclerViewAsteFrammentoOthersProfile.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewAsteFrammentoOthersProfile.adapter = auctionsLiveAdapter

        binding.cardBackButtonFrammentoAuctionDetails.setOnClickListener{
            parentFragmentManager.popBackStack()
        }


        auctionsLiveAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id, mailUtente, token))
            transaction?.addToBackStack(this.toString())
            transaction?.commit()
        }


        return binding.root
    }


    private fun setDatiBase(utente: UserBase){
        binding.imaveViewImmagineUtenteFrammentoOthersProfile.setImageBitmap(utente.immagine)
        binding.textViewNomeUtenteFrammentoOthersProfile.text = utente.nome
        binding.textViewValueMailFrammentoOthersProfile.text = utente.mail
        binding.textViewValueNazioneFrammentoOthersProfile.text = utente.nazione

        binding.textViewValueSitoWebFrammentoOthersProfile.text = utente.sito
        binding.textViewValueSocialFacebookFrammentoOthersProfile.text = utente.socialFacebook
        binding.textViewValueSocialInstagramFrammentoOthersProfile.text = utente.socialInstagram

        binding.textViewBiografiaValueFrammentoOthersProfile.text = utente.biografia

    }

    private fun setDatiBusiness(utenteBusiness: UserBusiness){
        setDatiBase(utenteBusiness.informazioniBase)
        binding.textViewValueRagioneSocialeFrammentoOthersProfile.text = utenteBusiness.ragioneSociale
        binding.textViewValuePartivaIvaFrammentoOthersProfile.text = utenteBusiness.partitaIva
        binding.textViewValueNumeroAziendaleFrammentoOthersProfile.text = utenteBusiness.numeroAziendale

    }

    private fun disattivaCampiBusiness(){
        binding.textViewLabelInfoBusinessFrammentoOthersProfile.isVisible = false
        binding.textViewLabelRagioneSocialeFrammentoOthersProfile.isVisible = false
        binding.textViewValueRagioneSocialeFrammentoOthersProfile.isVisible = false
        binding.textViewLabelPartitaIvaFrammentoOthersProfile.isVisible = false
        binding.textViewValuePartivaIvaFrammentoOthersProfile.isVisible = false
        binding.textViewLabelNumeroAziendaleFrammentoProfile.isVisible = false
        binding.textViewValueNumeroAziendaleFrammentoOthersProfile.isVisible = false
    }

    //Rende visibile i campi Business
    private fun attivaCampiBusiness(){
        binding.textViewLabelInfoBusinessFrammentoOthersProfile.isVisible = true
        binding.textViewLabelRagioneSocialeFrammentoOthersProfile.isVisible = true
        binding.textViewValueRagioneSocialeFrammentoOthersProfile.isVisible = true
        binding.textViewLabelPartitaIvaFrammentoOthersProfile.isVisible = true
        binding.textViewValuePartivaIvaFrammentoOthersProfile.isVisible = true
        binding.textViewLabelNumeroAziendaleFrammentoProfile.isVisible = true
        binding.textViewValueNumeroAziendaleFrammentoOthersProfile.isVisible = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDatiUtenteTerzi(mailUtenteTerzo: String){
        lifecycleScope.async {
            val response = try{
                RetrofitInstance.api.getDatiUtenteTerzi(mailUtenteTerzo, token)
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
                val immagineProfiloByteArrayDecoded = Base64.getDecoder().decode(utenteRecuperato.immagineProfilo)
                try{
                    var immagineProfilo= BitmapFactory.decodeByteArray(immagineProfiloByteArrayDecoded, 0, immagineProfiloByteArrayDecoded.size)
                    var indirizzoPlaceHolder = Indirizzo("","", "", "")
                    var utenteBase = UserBase(immagineProfilo, utenteRecuperato.nome, "", utenteRecuperato.mail, LocalDate.of(1970,1,1), utenteRecuperato.nazione, "", indirizzoPlaceHolder, indirizzoPlaceHolder, utenteRecuperato.sito, utenteRecuperato.socialFacebook, utenteRecuperato.socialInstagram, utenteRecuperato.biografia)
                    if(!utenteRecuperato.ragioneSociale.equals("")){
                        isBusiness = true
                        var utenteBusiness = UserBusiness(utenteBase, utenteRecuperato.ragioneSociale, utenteRecuperato.partitaIva, utenteRecuperato.numeroAziendale)
                        setDatiBusiness(utenteBusiness)
                        attivaCampiBusiness()
                        utente = utenteBusiness
                    }else{
                        isBusiness= false
                        setDatiBase(utenteBase)
                        disattivaCampiBusiness()
                        utente = UserBusiness(utenteBase, "", "", "")
                    }
                    getAsteUtenteTerzi(mailUtente, mailUtenteTerzo)
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
    private fun getAsteUtenteTerzi(email: String, mailUtenteTerzo: String){
        var auctionsCaricate = ArrayList<AuctionViewHistory>()
        lifecycleScope.async {
            val response = try{
                RetrofitInstance.api.getAsteUtenteTerzi(email, mailUtenteTerzo, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return@async
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO,"MyNetwork", "Response is successful")
                var asteRecuperate: List<AstaDTO> = response.body()!!
                for(asta in asteRecuperate){
                    val immagineAstaByteArrayDecoded = Base64.getDecoder().decode(asta.immagineAsta)
                    val immagineAsta= BitmapFactory.decodeByteArray(immagineAstaByteArrayDecoded, 0, immagineAstaByteArrayDecoded.size)
                    var auctionViewHistory = AuctionViewHistory(asta.idAsta, asta.titolo, asta.ultimaOfferta, ZonedDateTime.of(asta.anno, asta.mese, asta.giorno, 0, 0, 0,0, ZoneId.of("GMT")), immagineAsta, asta.emailCreatore, asta.offertaFatta)
                    auctionsCaricate.add(auctionViewHistory)
                }
                arrayAsteUtente = arrayAsteUtente + auctionsCaricate
                auctionsLiveAdapter.auctionsViewHistory = arrayAsteUtente
                auctionsLiveAdapter!!.notifyDataSetChanged()
                return@async
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


}