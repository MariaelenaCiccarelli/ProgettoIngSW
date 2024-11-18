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
import com.danilo.lootmarket.databinding.FragmentSubscribedAuctionsBinding
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.chrono.ChronoZonedDateTime

class SubscribedAuctionsFragment(private var auctions: List<AuctionViewHistory>, var mail: String, private var token: String ) : Fragment() {

    private lateinit var binding: FragmentSubscribedAuctionsBinding

    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auctions = auctions.filter { (it.dataScadenza.isAfter(ZonedDateTime.now())) }
        auctions = auctions.filter { (!it.autore.equals(mail)) }
        //Toast.makeText(this.requireContext(), auctions[0].offertaFatta.toString(), Toast.LENGTH_SHORT).show()
        auctionsLiveAdapter = AuctionsLiveAdapter(auctions, this.requireContext(), true)

        binding = FragmentSubscribedAuctionsBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        //auctionsLiveAdapter = AuctionsLiveAdapter(listAuction, this.requireContext())

        binding.RecyclerViewFrammentoSubscribedAuctions.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoSubscribedAuctions.adapter = auctionsLiveAdapter

        auctionsLiveAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id, mail, token))
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_subscribed_auctions, container, false)
        return binding.root
    }
}