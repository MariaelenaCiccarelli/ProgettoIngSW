package com.danilo.lootmarket.view.home.personal_auctions


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.view.adapters.AuctionsLiveAdapter
import com.danilo.lootmarket.R
import com.danilo.lootmarket.databinding.FragmentHistoryAuctionsBinding
import com.danilo.lootmarket.modelview.AuctionLiveView
import com.danilo.lootmarket.view.home.details.AuctionDetailsFragment
import java.time.ZonedDateTime

class HistoryAuctionsFragment(private var auctions: List<AuctionLiveView>, private var mailUtente: String, private var token: String): Fragment() {

    private lateinit var binding: FragmentHistoryAuctionsBinding

    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        auctions = auctions.filter { (it.dataScadenza.isBefore(ZonedDateTime.now())) }
        auctions = auctions.filter { (it.offertaFatta == true)}
        auctionsLiveAdapter = AuctionsLiveAdapter(auctions, true)


        binding = FragmentHistoryAuctionsBinding.inflate(layoutInflater)
        binding.RecyclerViewFrammentoHistoryAuctions.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoHistoryAuctions.adapter = auctionsLiveAdapter

        auctionsLiveAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id,mailUtente, token))
            transaction?.addToBackStack(this.tag)
            transaction?.commit()
        }

        return binding.root
    }
}