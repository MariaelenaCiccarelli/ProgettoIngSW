package com.danilo.lootmarket
import android.graphics.Bitmap
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
import com.danilo.lootmarket.databinding.FragmentOthersProfileBinding
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date


class OthersProfileFragment(private var mailUtente: String, private var token: String): Fragment() {

    private lateinit var binding: FragmentOthersProfileBinding

    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View? {
        // Inflate the layout for this fragment
        var auction1 = AuctionViewHistory(
            0,
            "Naruto",
            150.00,
            ZonedDateTime.now(),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Bitmap),
            "Prova",
            true
        )
        var auction2 = AuctionViewHistory(
            1,
            "Drago Bianco Occhi Blu Rara Ghost",
            15000.00,
            ZonedDateTime.of(2024, 9, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Bitmap),
            "Prova",
            false
        )
        var auction3 = AuctionViewHistory(
            2,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00,
            ZonedDateTime.of(2024, 10, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Bitmap),
            "Prova",
            false
        )
        var auction4 = AuctionViewHistory(
            3,
            "Tavola Stupenda One Piece",
            100.00,
            ZonedDateTime.of(2024, 9, 6, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Bitmap),
            "Prova",
            true
        )
        var auction5 = AuctionViewHistory(
            4,
            "Volume 33 Boruto",
            60.00,
            ZonedDateTime.of(2024, 9, 7, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Bitmap),
            "Prova",
            true
        )
        var auction6 = AuctionViewHistory(
            5,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00,
            ZonedDateTime.of(2024, 9, 10, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Bitmap),
            "Prova",
            false
        )
        var auction7 = AuctionViewHistory(
            6,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00,
            ZonedDateTime.of(2024, 11, 5, 23, 59, 59, 59, ZoneId.of("GMT")),
            (ResourcesCompat.getDrawable(resources, R.drawable.naruto, null) as Bitmap),
            "Prova",
            false
        )

        var auctions: List<AuctionViewHistory>
        auctions = listOf(auction1, auction2, auction3, auction4, auction5, auction6, auction7)


        binding = FragmentOthersProfileBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        //
        auctionsLiveAdapter = AuctionsLiveAdapter(auctions, this.requireContext(), false)

        binding.RecyclerViewAsteFrammentoOthersProfile.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewAsteFrammentoOthersProfile.adapter = auctionsLiveAdapter

        binding.cardBackButtonFrammentoAuctionDetails.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        auctionsLiveAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id, mailUtente, token))
            transaction?.addToBackStack(this.toString())
            transaction?.commit()
        }
        //val view = inflater.inflate(R.layout.fragment_home, container, false)
        return binding.root
    }
}