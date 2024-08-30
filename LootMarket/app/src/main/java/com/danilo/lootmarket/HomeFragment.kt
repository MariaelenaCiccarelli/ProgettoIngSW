package com.danilo.lootmarket
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import com.danilo.lootmarket.databinding.FragmentHomeBinding


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
            3,
            "prova",
            "Action figure originale in vinile di Naruto Uzumaki",
            "Action Figures"
        )
        var auction2 = Auction(
            0,
            "Drago Bianco Occhi Blu Rara Ghost",
            15000F,
            20,
            "prova",
            "Carta originale pazza incredibile di yu-gi-oh",
            "Carte Collezionabili"
        )
        var auction3 = Auction(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            0,
            "prova",
            "Me lo ha portato mio zio dal Giappone giuro su mio zio",
            "Gadget"
        )
        var auction4 = Auction(
            0,
            "Tavola Stupenda One Piece",
            100.00F,
            0,
            "prova",
            "C'è il One Piece",
            "Tavole"

        )
        var auction5 = Auction(
            0,
            "Volume 33 Boruto",
            60.00F,
            0,
            "prova",
            "Nessuno lo vuole",
            "Fumetti"
        )
        var auction6 = Auction(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            0,
            "prova",
            "Me lo ha portato mio zio dal Giappone giuro su mio zio",
            "Gadget"
        )
        var auction7 = Auction(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            0,
            "prova",
            "Me lo ha portato mio zio dal Giappone giuro su mio zio",
            "Gadget"
        )

        var auctions: List<Auction>
        auctions = listOf(auction1, auction2, auction3, auction4, auction5, auction6, auction7)


        binding = FragmentHomeBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        auctionsAdapter = AuctionsAdapter(auctions, this.requireContext())

        binding.RecyclerViewFrammentoHome.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoHome.adapter = auctionsAdapter
        //val view = inflater.inflate(R.layout.fragment_home, container, false)
        return binding.root
    }
}





