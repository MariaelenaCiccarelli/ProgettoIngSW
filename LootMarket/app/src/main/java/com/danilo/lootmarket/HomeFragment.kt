package com.danilo.lootmarket
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import com.danilo.lootmarket.databinding.FragmentHomeBinding
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date


class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var auctionsAdapter: AuctionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View? {
        // Inflate the layout for this fragment
        var auction1 = Auction(
            0,
            "Naruto",
            150F,
            ZonedDateTime.now(),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable),
            "Action figure originale in vinile di Naruto Uzumaki",
            "Action Figures"
        )
        var auction2 = Auction(
            0,
            "Drago Bianco Occhi Blu Rara Ghost",
            15000F,
            ZonedDateTime.of(2024, 9, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Carta originale pazza incredibile di yu-gi-oh",
            "Carte Collezionabili"
        )
        var auction3 = Auction(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            ZonedDateTime.of(2024, 10, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Me lo ha portato mio zio dal Giappone giuro su mio zio",
            "Gadget"
        )
        var auction4 = Auction(
            0,
            "Tavola Stupenda One Piece",
            100.00F,
            ZonedDateTime.of(2024, 9, 6, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "C'è il One Piece",
            "Tavole"

        )
        var auction5 = Auction(
            0,
            "Volume 33 Boruto",
            60.00F,
            ZonedDateTime.of(2024, 9, 7, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Nessuno lo vuole",
            "Fumetti"
        )
        var auction6 = Auction(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            ZonedDateTime.of(2024, 9, 10, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Me lo ha portato mio zio dal Giappone giuro su mio zio",
            "Gadget"
        )
        var auction7 = Auction(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            ZonedDateTime.of(2024, 11, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable),
            "Me lo ha portato mio zio dal Giappone giuro su mio zio",
            "Gadget"
        )

        var auctions: List<Auction>
        auctions = listOf(auction1, auction2, auction3, auction4, auction5, auction6, auction7)


        binding = FragmentHomeBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        var data = Date(10, 10,10)

        auctionsAdapter = AuctionsAdapter(auctions, this.requireContext())

        binding.RecyclerViewFrammentoHome.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoHome.adapter = auctionsAdapter
        //val view = inflater.inflate(R.layout.fragment_home, container, false)
        return binding.root
    }
}





