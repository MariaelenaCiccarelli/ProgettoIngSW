package com.danilo.lootmarket.view.home.personal_auctions

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi

import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Controller.Controller
import com.danilo.lootmarket.view.adapters.AuctionsLiveAdapter

import com.danilo.lootmarket.databinding.FragmentAuctionsBinding

import kotlinx.coroutines.async


class AuctionsFragment(var mail: String, var token: String) : Fragment() {
    private lateinit var binding: FragmentAuctionsBinding

    private lateinit var controller: Controller
    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        controller = Controller()
        binding = FragmentAuctionsBinding.inflate(layoutInflater)
        binding.cardBottoneIscrizioniFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#a91010"))
        binding.buttonIscrizioniFrammentoAuctions.setBackgroundColor(Color.parseColor("#a91010"))
        binding.buttonIscrizioniFrammentoAuctions.setTextColor(Color.parseColor("#FFF6DD"))
        auctionsLiveAdapter = AuctionsLiveAdapter(listOf())

        lifecycleScope.async {
            if (!controller.getAsteUtente(mail, token, auctionsLiveAdapter)) {
                activity?.finish()
            } else {
                replaceFragment(
                    SubscribedAuctionsFragment(
                        auctionsLiveAdapter.auctionsViewHistory,
                        mail,
                        token
                    )
                )
            }
        }




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







}