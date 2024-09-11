package com.danilo.lootmarket


import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.databinding.FragmentPersonalAuctionsBinding
import com.danilo.lootmarket.databinding.FragmentSubscribedAuctionsBinding
import java.time.ZoneId
import java.time.ZonedDateTime

class PersonalAuctionsFragment : Fragment() {

    private lateinit var binding: FragmentPersonalAuctionsBinding

    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var auction1 = AuctionViewHistory(
            0,
            "Naruto",
            150F,
            ZonedDateTime.now(),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable),
            "Naruto",
            true

        )
        var auction2 = AuctionViewHistory(
            0,
            "Drago Bianco Occhi Blu Rara Ghost",
            15000F,
            ZonedDateTime.of(2024, 9, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Sasuke",
            false

        )
        var auction3 = AuctionViewHistory(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            ZonedDateTime.of(2024, 10, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Mattia Brescia",
            true

        )
        var auction4 = AuctionViewHistory(
            0,
            "Tavola Stupenda One Piece",
            100.00F,
            ZonedDateTime.of(2024, 9, 6, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Sergio di Martino",
            true

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
        var auction7 = AuctionViewHistory(
            0,
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            150.00F,
            ZonedDateTime.of(2024, 11, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable),
            "Danilo Pellecchia",
            true

        )

        var auctions: List<AuctionViewHistory>
        auctions = listOf(auction1, auction2, auction3, auction4, auction5, auction6, auction7)


        binding = FragmentPersonalAuctionsBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        auctionsLiveAdapter = AuctionsLiveAdapter(auctions, this.requireContext(), false)

        binding.RecyclerViewFrammentoPersonalAuctions.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoPersonalAuctions.adapter = auctionsLiveAdapter



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_subscribed_auctions, container, false)
        return binding.root
    }
}