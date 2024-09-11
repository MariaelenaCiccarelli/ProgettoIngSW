package com.danilo.lootmarket


import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.databinding.FragmentHistoryAuctionsBinding
import com.danilo.lootmarket.databinding.FragmentSubscribedAuctionsBinding
import java.time.ZoneId
import java.time.ZonedDateTime

class HistoryAuctionsFragment : Fragment() {

    private lateinit var binding: FragmentHistoryAuctionsBinding

    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var auction2 = AuctionViewHistory(
            0,
            "Drago Bianco Occhi Blu Rara Ghost",
            15000F,
            ZonedDateTime.of(2024, 9, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Sasuke",
            false

        )
        var auction5 = AuctionViewHistory(
            0,
            "Volume 33 Boruto",
            60.00F,
            ZonedDateTime.of(2024, 9, 7, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Luigi Libero Lucio Starace",
            false
        )
        var auction6 = AuctionViewHistory(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            ZonedDateTime.of(2024, 9, 10, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Mariaelena Ciccarelli",
            false


        )

        var auctions: List<AuctionViewHistory>
        auctions = listOf( auction2, auction5, auction6)


        binding = FragmentHistoryAuctionsBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        auctionsLiveAdapter = AuctionsLiveAdapter(auctions, this.requireContext())

        binding.RecyclerViewFrammentoHistoryAuctions.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoHistoryAuctions.adapter = auctionsLiveAdapter



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_subscribed_auctions, container, false)
        return binding.root
    }
}