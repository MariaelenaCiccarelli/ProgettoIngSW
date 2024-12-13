package com.danilo.lootmarket.view.home.personal_auctions


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.view.adapters.AuctionsLiveAdapter
import com.danilo.lootmarket.R
import com.danilo.lootmarket.databinding.FragmentPersonalAuctionsBinding
import com.danilo.lootmarket.modelview.AuctionLiveView
import com.danilo.lootmarket.view.home.details.AuctionDetailsFragment

class PersonalAuctionsFragment(private var auctions: List<AuctionLiveView>, var mail: String, private var token: String) : Fragment() {

    private lateinit var binding: FragmentPersonalAuctionsBinding

    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{


        binding = FragmentPersonalAuctionsBinding.inflate(layoutInflater)
        auctions = auctions.filter { (it.autore.equals(mail)) }
        auctionsLiveAdapter = AuctionsLiveAdapter(auctions, false)
        binding.RecyclerViewFrammentoPersonalAuctions.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoPersonalAuctions.adapter = auctionsLiveAdapter

        auctionsLiveAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id,mail, token))
            transaction?.addToBackStack(this.tag)
            transaction?.commit()
        }

        return binding.root
    }


}