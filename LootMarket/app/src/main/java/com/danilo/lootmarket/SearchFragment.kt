package com.danilo.lootmarket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danilo.lootmarket.databinding.FragmentSearchBinding
import java.util.Locale


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var auctionsAdapter: AuctionsAdapter
    private lateinit var searchView: SearchView
    private lateinit var searchList: ArrayList<Auction>


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
            "Action figure originale in vinile di Naruto Uzumaki"
        )
        var auction2 = Auction(
            0,
            "Drago Bianco Occhi Blu Rara Ghost",
            15000F,
            20,
            "prova",
            "Carta originale pazza incredibile di yu-gi-oh"
        )
        var auction3 = Auction(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            0,
            "prova",
            "Me lo ha portato mio zio dal Giappone giuro su mio zio"
        )
        var auction4 = Auction(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            0,
            "prova",
            "Me lo ha portato mio zio dal Giappone giuro su mio zio"
        )
        var auction5 = Auction(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            0,
            "prova",
            "Me lo ha portato mio zio dal Giappone giuro su mio zio"
        )
        var auction6 = Auction(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            0,
            "prova",
            "Me lo ha portato mio zio dal Giappone giuro su mio zio"
        )
        var auction7 = Auction(
            0,
            "Pennino Originale Giuro di Masashi Kishimoto",
            150.00F,
            0,
            "prova",
            "Me lo ha portato mio zio dal Giappone giuro su mio zio"
        )

        var auctions: List<Auction>
        auctions = listOf(auction1, auction2, auction3, auction4, auction5, auction6, auction7)
        searchList = arrayListOf<Auction>()


        binding = FragmentSearchBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        auctionsAdapter = AuctionsAdapter(auctions, this.requireContext())
        binding.RecyclerViewFrammentoSearch.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoSearch.adapter = auctionsAdapter
        searchView = binding.searchbarFrammentoSearch.findViewById(R.id.searchbarFrammentoSearch)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    auctions.forEach{
                        if(it.titoloAsta.toLowerCase(Locale.getDefault()).contains(searchText)){
                            searchList.add(it)
                        }
                    }
                    binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
                }else{
                    searchList.clear()
                    searchList.addAll(auctions)
                    binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
                }
                return false

            }
        })


        //val view = inflater.inflate(R.layout.fragment_home, container, false)
        return binding.root
    }
}