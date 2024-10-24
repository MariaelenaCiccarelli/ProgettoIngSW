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

class SubscribedAuctionsFragment(private var auctions: List<AuctionViewHistory> ) : Fragment() {

    private lateinit var binding: FragmentSubscribedAuctionsBinding

    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var auction1 = AuctionViewHistory(
            0,
            "Naruto",
            150.00,
            ZonedDateTime.now(),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable),
            "Naruto",
            true

        )
        var auction2 = AuctionViewHistory(
            0,
            "Drago Bianco Occhi Blu Rara Ghost",
            15000.00,
            ZonedDateTime.of(2024, 9, 12, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Sasuke",
            false

        )
        var auction3 = AuctionViewHistory(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00,
            ZonedDateTime.of(2024, 10, 11, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Mattia Brescia",
            true

        )
        var auction4 = AuctionViewHistory(
            0,
            "Tavola Stupenda One Piece",
            100.00,
            ZonedDateTime.now().plusDays(1),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Sergio di Martino",
            true

        )
        var auction5 = AuctionViewHistory(
            0,
            "Volume 33 Boruto",
            60.00,
            ZonedDateTime.of(2024, 9, 7, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Luigi Libero Lucio Starace",
            false
        )
        var auction6 = AuctionViewHistory(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00,
            ZonedDateTime.of(2024, 9, 10, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Mariaelena Ciccarelli",
            false
        )
        var auction7 = AuctionViewHistory(
            0,
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            150.00,
            ZonedDateTime.of(2024, 11, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable),
            "Danilo Pellecchia",
            true

        )




        auctions = auctions.filter { (it.dataScadenza.isAfter(ZonedDateTime.now())) }
        auctions = auctions.filter { (!it.autore.equals("danilo@mail.it")) }
        //Toast.makeText(this.requireContext(), auctions[0].offertaFatta.toString(), Toast.LENGTH_SHORT).show()
        auctionsLiveAdapter = AuctionsLiveAdapter(auctions, this.requireContext(), true)

        binding = FragmentSubscribedAuctionsBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        //auctionsLiveAdapter = AuctionsLiveAdapter(listAuction, this.requireContext())

        binding.RecyclerViewFrammentoSubscribedAuctions.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoSubscribedAuctions.adapter = auctionsLiveAdapter

        auctionsLiveAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id))
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_subscribed_auctions, container, false)
        return binding.root
    }
}