package com.danilo.lootmarket

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danilo.lootmarket.databinding.FragmentSearchBinding
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var auctionsAdapter: AuctionsAdapter
    private lateinit var searchView: SearchView
    private lateinit var filteredList: ArrayList<Auction>
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
        filteredList = arrayListOf<Auction>()
        searchList = arrayListOf<Auction>()
        filteredList.addAll(auctions)
        searchList.addAll(filteredList)


        binding = FragmentSearchBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        auctionsAdapter = AuctionsAdapter(searchList, this.requireContext())
        binding.RecyclerViewFrammentoSearch.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoSearch.adapter = auctionsAdapter
        searchView = binding.searchbarFrammentoSearch
        searchView.clearFocus()



        var statusFiltroFumetti: Boolean = false
        var statusFiltroActionFigures: Boolean = false
        var statusFiltroCarte: Boolean = false
        var statusFiltroTavole: Boolean = false
        var statusFiltroGadget: Boolean = false
        var filterCounter: Int = 0;


        //FILTRO FUMETTI
        binding.cardFiltroFumettiFrammentoSearch.setOnClickListener {
            if (!statusFiltroFumetti) {
                binding.immagineFiltroFumettiFrammentoSearch.setImageResource(R.drawable.baseline_auto_stories_24_secondcolor)
                binding.cardFiltroFumettiFrammentoSearch.setCardBackgroundColor(Color.parseColor("#a91010"))
                if(filterCounter==0){
                    filteredList.clear()
                }
                filteredList.addAll(auctions.filter { it.Categoria == "Fumetti" })
                searchList.clear()
                searchList.addAll(filteredList)
                statusFiltroFumetti = true
                filterCounter++
                binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
            } else {
                filterCounter--
                if(filterCounter==0){
                    filteredList.clear()
                    filteredList.addAll(auctions)
                    searchList.clear()
                    searchList.addAll(filteredList)
                }else{
                    searchList.clear()
                    searchList.addAll(filteredList.filter { it.Categoria != "Fumetti" })
                    filteredList.clear()
                    filteredList.addAll(searchList)
                }
                statusFiltroFumetti = false
                binding.immagineFiltroFumettiFrammentoSearch.setImageResource(R.drawable.baseline_auto_stories_24)
                binding.cardFiltroFumettiFrammentoSearch.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
                binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
            }
        }

        //FILTRO ACTION FIGURES
        binding.cardFiltroActionFiguresFrammentoSearch.setOnClickListener{
            if(!statusFiltroActionFigures){
                binding.immagineFiltroActionFiguresFrammentoSearch.setImageResource(R.drawable.baseline_man_4_24_secondcolor)
                binding.cardFiltroActionFiguresFrammentoSearch.setCardBackgroundColor(Color.parseColor("#a91010"))
                if(filterCounter==0){
                    filteredList.clear()
                }
                filteredList.addAll(auctions.filter { it.Categoria == "Action Figures" })
                searchList.clear()
                searchList.addAll(filteredList)
                statusFiltroActionFigures = true
                filterCounter++
                binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
            }else{
                filterCounter--
                if(filterCounter==0){
                    filteredList.clear()
                    filteredList.addAll(auctions)
                    searchList.clear()
                    searchList.addAll(filteredList)
                }else{
                    searchList.clear()
                    searchList.addAll(filteredList.filter { it.Categoria != "Action Figures" })
                    filteredList.clear()
                    filteredList.addAll(searchList)
                }
                statusFiltroActionFigures = false

                binding.immagineFiltroActionFiguresFrammentoSearch.setImageResource(R.drawable.baseline_man_4_24)
                binding.cardFiltroActionFiguresFrammentoSearch.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
                binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
            }
        }

        //FILTRO CARTE
        binding.cardFiltroCarteFrammentoSearch.setOnClickListener {

            if (!statusFiltroCarte) {
                binding.immagineFiltroCarteFrammentoSearch.setImageResource(R.drawable.baseline_screenshot_24_secondcolor)
                binding.cardFiltroCarteFrammentoSearch.setCardBackgroundColor(Color.parseColor("#a91010"))
                if(filterCounter==0){
                    filteredList.clear()
                }
                filteredList.addAll(auctions.filter { it.Categoria == "Carte Collezionabili" })
                searchList.clear()
                searchList.addAll(filteredList)
                statusFiltroCarte = true
                filterCounter++
                binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
            } else {
                filterCounter--
                if(filterCounter==0){
                    filteredList.clear()
                    filteredList.addAll(auctions)
                    searchList.clear()
                    searchList.addAll(filteredList)
                }else{
                    searchList.clear()
                    searchList.addAll(filteredList.filter { it.Categoria != "Carte Collezionabili" })
                    filteredList.clear()
                    filteredList.addAll(searchList)
                }
                statusFiltroCarte = false

                binding.immagineFiltroCarteFrammentoSearch.setImageResource(R.drawable.baseline_screenshot_24)
                binding.cardFiltroCarteFrammentoSearch.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
                binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
            }

        }

        //FILTRO TAVOLE
        binding.cardFiltroTavoleFrammentoSearch.setOnClickListener {
            if (!statusFiltroTavole) {
                binding.immagineFiltroTavoleFrammentoSearch.setImageResource(R.drawable.baseline_brush_24_secondcolor)
                binding.cardFiltroTavoleFrammentoSearch.setCardBackgroundColor(Color.parseColor("#a91010"))
                if(filterCounter==0){
                    filteredList.clear()
                }
                filteredList.addAll(auctions.filter { it.Categoria == "Tavole" })
                searchList.clear()
                searchList.addAll(filteredList)
                statusFiltroTavole = true
                filterCounter++
                binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
            } else {
                filterCounter--
                if(filterCounter==0){
                    filteredList.clear()
                    filteredList.addAll(auctions)
                    searchList.clear()
                    searchList.addAll(filteredList)
                }else{
                    searchList.clear()
                    searchList.addAll(filteredList.filter { it.Categoria != "Tavole" })
                    filteredList.clear()
                    filteredList.addAll(searchList)
                }
                statusFiltroTavole = false

                binding.immagineFiltroTavoleFrammentoSearch.setImageResource(R.drawable.baseline_brush_24)
                binding.cardFiltroTavoleFrammentoSearch.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
                binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
            }
        }

        //FILTRO GADGET
        binding.cardFiltroGadgetFrammentoSearch.setOnClickListener {
            if (!statusFiltroGadget) {
                binding.immagineFiltroGadgetFrammentoSearch.setImageResource(R.drawable.baseline_catching_pokemon_24_secondcolor)
                binding.cardFiltroGadgetFrammentoSearch.setCardBackgroundColor(Color.parseColor("#a91010"))
                if(filterCounter==0){
                    filteredList.clear()
                }
                filteredList.addAll(auctions.filter { it.Categoria == "Gadget" })
                searchList.clear()
                searchList.addAll(filteredList)
                statusFiltroGadget = true
                filterCounter++
                binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
            } else {
                filterCounter--
                if(filterCounter==0){
                    filteredList.clear()
                    filteredList.addAll(auctions)
                    searchList.clear()
                    searchList.addAll(filteredList)
                }else{
                    searchList.clear()
                    searchList.addAll(filteredList.filter { it.Categoria != "Gadget" })
                    filteredList.clear()
                    filteredList.addAll(searchList)
                }
                statusFiltroGadget = false
                binding.immagineFiltroGadgetFrammentoSearch.setImageResource(R.drawable.baseline_catching_pokemon_24)
                binding.cardFiltroGadgetFrammentoSearch.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
                binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
            }
        }



        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    filteredList.forEach{
                        if(it.titoloAsta.toLowerCase(Locale.getDefault()).contains(searchText)){
                            searchList.add(it)
                        }
                    }
                    binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
                }else{
                    searchList.clear()
                    searchList.addAll(filteredList)
                    binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
                }
                return false

            }

        })

        //val view = inflater.inflate(R.layout.fragment_home, container, false)
        return binding.root
    }
}