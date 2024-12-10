package com.danilo.lootmarket

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope

import com.danilo.lootmarket.databinding.FragmentAuctionsBinding
import com.danilo.lootmarket.network.RetrofitInstance
import com.danilo.lootmarket.network.dto.AstaDTO

import kotlinx.coroutines.async
import okio.IOException
import retrofit2.HttpException
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Base64


class AuctionsFragment(var mail: String, var token: String) : Fragment() {
    private lateinit var binding: FragmentAuctionsBinding
    private lateinit var arrayAsteUtente: List<AuctionViewHistory>
    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuctionsBinding.inflate(layoutInflater)
        binding.cardBottoneIscrizioniFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#a91010"))
        binding.buttonIscrizioniFrammentoAuctions.setBackgroundColor(Color.parseColor("#a91010"))
        binding.buttonIscrizioniFrammentoAuctions.setTextColor(Color.parseColor("#FFF6DD"))

        arrayAsteUtente = listOf()
        getAsteUtente(mail)
        auctionsLiveAdapter = AuctionsLiveAdapter(arrayAsteUtente)


        binding.buttonProprieFrammentoAuctions.setOnClickListener{

            binding.cardBottoneProprieFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#a91010"))
            binding.buttonProprieFrammentoAuctions.setBackgroundColor(Color.parseColor("#a91010"))
            binding.buttonProprieFrammentoAuctions.setTextColor(Color.parseColor("#FFF6DD"))

            binding.cardBottoneIscrizioniFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonIscrizioniFrammentoAuctions.setBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonIscrizioniFrammentoAuctions.setTextColor(Color.parseColor("#4d251b"))

            binding.cardBottoneStoricoFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonStoricoFrammentoAuctions.setBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonStoricoFrammentoAuctions.setTextColor(Color.parseColor("#4d251b"))

            replaceFragment(PersonalAuctionsFragment(auctionsLiveAdapter.auctionsViewHistory, mail, token))
        }


        binding.buttonIscrizioniFrammentoAuctions.setOnClickListener{

            binding.cardBottoneIscrizioniFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#a91010"))
            binding.buttonIscrizioniFrammentoAuctions.setBackgroundColor(Color.parseColor("#a91010"))
            binding.buttonIscrizioniFrammentoAuctions.setTextColor(Color.parseColor("#FFF6DD"))

            binding.cardBottoneProprieFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonProprieFrammentoAuctions.setBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonProprieFrammentoAuctions.setTextColor(Color.parseColor("#4d251b"))

            binding.cardBottoneStoricoFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonStoricoFrammentoAuctions.setBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonStoricoFrammentoAuctions.setTextColor(Color.parseColor("#4d251b"))

            replaceFragment(SubscribedAuctionsFragment(auctionsLiveAdapter.auctionsViewHistory, mail, token))
        }
        binding.buttonStoricoFrammentoAuctions.setOnClickListener{

            binding.cardBottoneStoricoFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#a91010"))
            binding.buttonStoricoFrammentoAuctions.setBackgroundColor(Color.parseColor("#a91010"))
            binding.buttonStoricoFrammentoAuctions.setTextColor(Color.parseColor("#FFF6DD"))

            binding.cardBottoneIscrizioniFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonIscrizioniFrammentoAuctions.setBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonIscrizioniFrammentoAuctions.setTextColor(Color.parseColor("#4d251b"))

            binding.cardBottoneProprieFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonProprieFrammentoAuctions.setBackgroundColor(Color.parseColor("#FFF6DD"))
            binding.buttonProprieFrammentoAuctions.setTextColor(Color.parseColor("#4d251b"))

            replaceFragment(HistoryAuctionsFragment(auctionsLiveAdapter.auctionsViewHistory, mail, token))
        }


        return binding.root
    }





    private fun replaceFragment(fragment: Fragment){
        var transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(binding.frameContainerFrammentoAuctions.id, fragment).commit()
    }





    private fun getAsteUtente(email: String){
        var auctionsCaricate = ArrayList<AuctionViewHistory>()
        lifecycleScope.async {
            val response = try{
                RetrofitInstance.api.getAsteUtente(email, token)
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
                replaceFragment(SubscribedAuctionsFragment(auctionsLiveAdapter.auctionsViewHistory, mail, token))
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