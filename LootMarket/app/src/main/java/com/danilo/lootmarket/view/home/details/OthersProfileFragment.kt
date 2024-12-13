package com.danilo.lootmarket.view.home.details

import android.os.Build
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Controller.Controller
import com.danilo.lootmarket.modelview.UserBaseView
import com.danilo.lootmarket.modelview.UserBusinessView
import com.danilo.lootmarket.R
import com.danilo.lootmarket.view.adapters.AuctionsLiveAdapter
import com.danilo.lootmarket.databinding.FragmentOthersProfileBinding
import kotlinx.coroutines.async



class OthersProfileFragment(private var mailUtente: String, private var token: String, private var mailUtenteTerzo: String): Fragment() {

    private lateinit var binding: FragmentOthersProfileBinding

    private lateinit var controller: Controller
    private var isBusiness: Boolean = false
    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View{
        var utente: UserBusinessView?
        auctionsLiveAdapter = AuctionsLiveAdapter(listOf(), false)


        binding = FragmentOthersProfileBinding.inflate(layoutInflater)
        controller = Controller()
        lifecycleScope.async {
            utente = controller.getDatiUtenteTerzi(mailUtenteTerzo, token)
            if(utente == null){
                activity?.finish()
            }else {
                if (utente!!.ragioneSociale == "") {
                    isBusiness= false
                    setDatiBase(utente!!.informazioniBase)
                    disattivaCampiBusiness()
                }else{
                    isBusiness=true
                    setDatiBusiness(utente!!)
                    attivaCampiBusiness()
                }
                if(!(controller.getAsteUtenteTerzi(mailUtente, mailUtenteTerzo, token, auctionsLiveAdapter))){
                    activity?.finish()
                }else{

                }
            }
        }



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


    private fun setDatiBase(utente: UserBaseView){
        binding.imaveViewImmagineUtenteFrammentoOthersProfile.setImageBitmap(utente.immagine)
        binding.textViewNomeUtenteFrammentoOthersProfile.text = utente.nome
        binding.textViewValueMailFrammentoOthersProfile.text = utente.mail
        binding.textViewValueNazioneFrammentoOthersProfile.text = utente.nazione
        if(utente.sito!=""){
            binding.textViewValueSitoWebFrammentoOthersProfile.text = utente.sito
        }else{
            binding.textViewValueSitoWebFrammentoOthersProfile.text = "Non presente"
        }
        if(utente.socialFacebook!=""){
            binding.textViewValueSocialFacebookFrammentoOthersProfile.text = utente.socialFacebook
        }else{
            binding.textViewValueSocialFacebookFrammentoOthersProfile.text = "Non presente"
        }
        if(utente.socialInstagram!=""){
            binding.textViewValueSocialInstagramFrammentoOthersProfile.text = utente.socialInstagram
        }else{
            binding.textViewValueSocialInstagramFrammentoOthersProfile.text = "Non presente"
        }
        if(utente.biografia!=""){
            binding.textViewBiografiaValueFrammentoOthersProfile.text = utente.biografia
        }else{
            binding.textViewBiografiaValueFrammentoOthersProfile.text = "Non presente"
        }
    }

    private fun setDatiBusiness(utenteBusiness: UserBusinessView){
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

}