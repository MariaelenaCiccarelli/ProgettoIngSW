package com.danilo.lootmarket

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.danilo.lootmarket.databinding.FragmentAuctionsBinding
import com.danilo.lootmarket.databinding.FragmentHomeBinding


class AuctionsFragment : Fragment() {
    private lateinit var binding: FragmentAuctionsBinding
    private lateinit var childFragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuctionsBinding.inflate(layoutInflater)
        binding.cardBottoneIscrizioniFrammentoAuctions.setCardBackgroundColor(Color.parseColor("#a91010"))
        binding.buttonIscrizioniFrammentoAuctions.setBackgroundColor(Color.parseColor("#a91010"))
        binding.buttonIscrizioniFrammentoAuctions.setTextColor(Color.parseColor("#FFF6DD"))


        replaceFragment(SubscribedAuctionsFragment())

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

            replaceFragment(PersonalAuctionsFragment())
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


            replaceFragment(SubscribedAuctionsFragment())
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

            replaceFragment(HistoryAuctionsFragment())
        }

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment){

        var transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(binding.frameContainerFrammentoAuctions.id, fragment).commit()
        //childFragmentManager.beginTransaction().replace(binding.frameContainerFrammentoAuctions.id, fragment).commit()

    }

}