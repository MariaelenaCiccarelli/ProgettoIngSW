package com.danilo.lootmarket


import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.databinding.FragmentHistoryAuctionsBinding
import com.danilo.lootmarket.databinding.FragmentSubscribedAuctionsBinding
import java.time.ZoneId
import java.time.ZonedDateTime

class HistoryAuctionsFragment(private var auctions: List<AuctionViewHistory>, private var mailUtente: String, private var token: String): Fragment() {

    private lateinit var binding: FragmentHistoryAuctionsBinding

    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auctions = auctions.filter { (it.dataScadenza.isBefore(ZonedDateTime.now())) }
        auctions = auctions.filter { (it.offertaFatta == true)}

        auctionsLiveAdapter = AuctionsLiveAdapter(auctions, this.requireContext(), true)


        binding = FragmentHistoryAuctionsBinding.inflate(layoutInflater)

        binding.RecyclerViewFrammentoHistoryAuctions.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoHistoryAuctions.adapter = auctionsLiveAdapter

        auctionsLiveAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id,mailUtente, token))
            transaction?.addToBackStack(this.tag)
            transaction?.commit()
        }



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_subscribed_auctions, container, false)
        return binding.root
    }
}