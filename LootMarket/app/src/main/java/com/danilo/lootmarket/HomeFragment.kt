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
            150.00,
            ZonedDateTime.now(),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable),
            "Action figure originale in vinile di Naruto Uzumaki",
            "Action Figures",
            "Asta inversa"
        )
        var auction2 = Auction(
            1,
            "Drago Bianco Occhi Blu Rara Ghost",
            15000.00,
            ZonedDateTime.of(2024, 9, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Carta originale pazza incredibile di yu-gi-oh",
            "Carte Collezionabili",
            "Asta a tempo fisso"
        )
        var auction3 = Auction(
            2,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00,
            ZonedDateTime.of(2024, 10, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Me lo ha portato mio zio dal Giappone giuro su mio zio",
            "Gadget",
            "Asta a tempo fisso"
        )
        var auction4 = Auction(
            3,
            "Tavola Stupenda One Piece",
            100.00,
            ZonedDateTime.of(2024, 9, 6, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "C'è il One Piece",
            "Tavole",
            "Asta a tempo fisso"

        )
        var auction5 = Auction(
            4,
            "Volume 33 Boruto",
            60.00,
            ZonedDateTime.of(2024, 9, 7, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Nessuno lo vuole",
            "Fumetti",
            "Asta a tempo fisso"
        )
        var auction6 = Auction(
            5,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00,
            ZonedDateTime.of(2024, 9, 10, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable),
            "Me lo ha portato mio zio dal Giappone giuro su mio zio",
            "Gadget",
            "Asta inversa"
        )
        var auction7 = Auction(
            6,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00,
            ZonedDateTime.of(2024, 11, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Drawable),
            "Me lo ha portato mio zio dal Giappone giuro su mio zio",
            "Gadget",
            "Asta inversa"
        )

        var auctions: List<Auction>
        auctions = listOf(auction1, auction2, auction3, auction4, auction5, auction6, auction7)


        binding = FragmentHomeBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        auctionsAdapter = AuctionsAdapter(auctions, this.requireContext())

        binding.RecyclerViewFrammentoHome.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoHome.adapter = auctionsAdapter

        auctionsAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id))
            transaction?.addToBackStack(this.toString())
            transaction?.commit()
        }

        //val view = inflater.inflate(R.layout.fragment_home, container, false)
        return binding.root
    }
}





