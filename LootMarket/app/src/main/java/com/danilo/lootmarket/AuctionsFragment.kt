package com.danilo.lootmarket

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Network.RetrofitInstance
import com.danilo.lootmarket.Network.dto.AstaDTO
import com.danilo.lootmarket.databinding.FragmentAuctionsBinding
import com.danilo.lootmarket.databinding.FragmentHomeBinding
import kotlinx.coroutines.async
import okio.IOException
import retrofit2.HttpException
import java.time.ZoneId
import java.time.ZonedDateTime


class AuctionsFragment : Fragment() {
    private lateinit var binding: FragmentAuctionsBinding
    private lateinit var childFragmentManager: FragmentManager
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
        getAsteUtente("danilo@mail.it")
        //Toast.makeText(this.context, arrayAsteUtente.get(0).titoloAsta, Toast.LENGTH_SHORT).show()


        auctionsLiveAdapter = AuctionsLiveAdapter(arrayAsteUtente, this.requireContext())





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

            replaceFragment(PersonalAuctionsFragment(auctionsLiveAdapter.auctionsViewHistory))
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


            replaceFragment(SubscribedAuctionsFragment(auctionsLiveAdapter.auctionsViewHistory))
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

            replaceFragment(HistoryAuctionsFragment(auctionsLiveAdapter.auctionsViewHistory))
        }



        return binding.root
    }

    private fun replaceFragment(fragment: Fragment){

        var transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(binding.frameContainerFrammentoAuctions.id, fragment).commit()
        //childFragmentManager.beginTransaction().replace(binding.frameContainerFrammentoAuctions.id, fragment).commit()

    }

    private fun getAsteUtente(email: String){
        var auctionsCaricate = ArrayList<AuctionViewHistory>()
        lifecycleScope.async {

            val response = try{
                RetrofitInstance.api.getAsteUtente(email)
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
                    Log.println(Log.INFO, "MyNetwork", asta.toString())
                    var auctionViewHistory = AuctionViewHistory(asta.idAsta, asta.titolo, asta.ultimaOfferta, ZonedDateTime.of(asta.anno, asta.mese, asta.giorno, 0, 0, 0,0, ZoneId.of("GMT")), (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable), asta.emailCreatore, asta.offertaFatta)
                    auctionsCaricate.add(auctionViewHistory)

                    Log.println(Log.INFO, "MyNetwork", auctionsCaricate[0].titoloAsta)
                }
                arrayAsteUtente = arrayAsteUtente + auctionsCaricate
                auctionsLiveAdapter.auctionsViewHistory = arrayAsteUtente
                auctionsLiveAdapter!!.notifyDataSetChanged()

                replaceFragment(SubscribedAuctionsFragment(auctionsLiveAdapter.auctionsViewHistory))
                return@async
            }else{
                Log.e("MyNetwork", "Response not successful")
                return@async
            }
        }

    }
}