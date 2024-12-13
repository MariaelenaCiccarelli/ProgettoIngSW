package com.danilo.lootmarket.view.home


import android.graphics.Color
import android.os.Build
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danilo.lootmarket.Controller.Controller
import com.danilo.lootmarket.modelview.AuctionView
import com.danilo.lootmarket.R
import com.danilo.lootmarket.view.adapters.AuctionsAdapter

import com.danilo.lootmarket.databinding.FragmentSearchBinding
import com.danilo.lootmarket.view.home.details.AuctionDetailsFragment
import kotlinx.coroutines.async

import java.util.Locale


class SearchFragment(var mail: String, var token: String) : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var auctionsAdapter: AuctionsAdapter
    private lateinit var searchView: SearchView
    private lateinit var filteredList: ArrayList<AuctionView>
    private lateinit var searchList: ArrayList<AuctionView>
    private lateinit var auctionViews: List<AuctionView>
    private lateinit var controller: Controller
    private var indice: Int = 0
    private var statusFiltroFumetti: Boolean = false
    private var statusFiltroActionFigures: Boolean = false
    private var statusFiltroCarte: Boolean = false
    private var statusFiltroTavole: Boolean = false
    private var statusFiltroGadget: Boolean = false
    private var filterCounter: Int = 0;
    private var filtroTestuale: String = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View{
        controller = Controller()
        indice=0
        auctionViews = listOf()

        lifecycleScope.async {
            auctionViews = auctionViews + controller.getAsteSearch(indice++, token)
            applicaFiltri()
        }



        filteredList = arrayListOf<AuctionView>()
        searchList = arrayListOf<AuctionView>()
        filteredList.addAll(auctionViews)
        searchList.addAll(filteredList)


        binding = FragmentSearchBinding.inflate(layoutInflater)

        auctionsAdapter = AuctionsAdapter(searchList)
        binding.RecyclerViewFrammentoSearch.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoSearch.adapter = auctionsAdapter
        searchView = binding.searchbarFrammentoSearch
        searchView.clearFocus()



        //FILTRO FUMETTI
        binding.cardFiltroFumettiFrammentoSearch.setOnClickListener {
            if (!statusFiltroFumetti) {
                binding.immagineFiltroFumettiFrammentoSearch.setImageResource(R.drawable.baseline_auto_stories_24_secondcolor)
                binding.cardFiltroFumettiFrammentoSearch.setCardBackgroundColor(Color.parseColor("#a91010"))
                statusFiltroFumetti = true
                filterCounter++
                applicaFiltri()
            } else {
                statusFiltroFumetti = false
                filterCounter--
                applicaFiltri()
                binding.immagineFiltroFumettiFrammentoSearch.setImageResource(R.drawable.baseline_auto_stories_24)
                binding.cardFiltroFumettiFrammentoSearch.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
            }
        }



        //FILTRO ACTION FIGURES
        binding.cardFiltroActionFiguresFrammentoSearch.setOnClickListener{
            if(!statusFiltroActionFigures){
                binding.immagineFiltroActionFiguresFrammentoSearch.setImageResource(R.drawable.baseline_man_4_24_secondcolor)
                binding.cardFiltroActionFiguresFrammentoSearch.setCardBackgroundColor(Color.parseColor("#a91010"))
                statusFiltroActionFigures = true
                filterCounter++
                applicaFiltri()
            }else{
                statusFiltroActionFigures = false
                filterCounter--
                applicaFiltri()
                binding.immagineFiltroActionFiguresFrammentoSearch.setImageResource(R.drawable.baseline_man_4_24)
                binding.cardFiltroActionFiguresFrammentoSearch.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
            }
        }



        //FILTRO CARTE
        binding.cardFiltroCarteFrammentoSearch.setOnClickListener {
            if (!statusFiltroCarte) {
                binding.immagineFiltroCarteFrammentoSearch.setImageResource(R.drawable.baseline_screenshot_24_secondcolor)
                binding.cardFiltroCarteFrammentoSearch.setCardBackgroundColor(Color.parseColor("#a91010"))
                statusFiltroCarte = true
                filterCounter++
                applicaFiltri()
            } else {
                statusFiltroCarte = false
                filterCounter--
                applicaFiltri()
                binding.immagineFiltroCarteFrammentoSearch.setImageResource(R.drawable.baseline_screenshot_24)
                binding.cardFiltroCarteFrammentoSearch.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
            }

        }



        //FILTRO TAVOLE
        binding.cardFiltroTavoleFrammentoSearch.setOnClickListener {
            if (!statusFiltroTavole) {
                binding.immagineFiltroTavoleFrammentoSearch.setImageResource(R.drawable.baseline_brush_24_secondcolor)
                binding.cardFiltroTavoleFrammentoSearch.setCardBackgroundColor(Color.parseColor("#a91010"))
                statusFiltroTavole = true
                filterCounter++
                applicaFiltri()
            } else {
                statusFiltroTavole = false
                filterCounter--
                applicaFiltri()
                binding.immagineFiltroTavoleFrammentoSearch.setImageResource(R.drawable.baseline_brush_24)
                binding.cardFiltroTavoleFrammentoSearch.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
            }
        }



        //FILTRO GADGET
        binding.cardFiltroGadgetFrammentoSearch.setOnClickListener {
            if (!statusFiltroGadget) {
                binding.immagineFiltroGadgetFrammentoSearch.setImageResource(R.drawable.baseline_catching_pokemon_24_secondcolor)
                binding.cardFiltroGadgetFrammentoSearch.setCardBackgroundColor(Color.parseColor("#a91010"))
                statusFiltroGadget = true
                filterCounter++
                applicaFiltri()
            } else {
                statusFiltroGadget = false
                filterCounter--
                applicaFiltri()
                binding.immagineFiltroGadgetFrammentoSearch.setImageResource(R.drawable.baseline_catching_pokemon_24)
                binding.cardFiltroGadgetFrammentoSearch.setCardBackgroundColor(Color.parseColor("#FFF6DD"))
            }
        }



        //Ricerca per testo
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                filtroTestuale = searchText
                if(searchText.isNotEmpty()){
                    filteredList.forEach{
                        if(it.titoloAsta.toLowerCase(Locale.getDefault()).contains(searchText)){
                            searchList.add(it)
                        }
                    }
                    if(searchList.isEmpty()){
                        Toast.makeText(context, "Nessuna asta trovata!", Toast.LENGTH_SHORT).show()
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



        //Click su un Asta
        auctionsAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id, mail, token))
            transaction?.addToBackStack(this.tag)
            transaction?.commit()
        }



        //caricamento ulteriori aste
        binding.RecyclerViewFrammentoSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lifecycleScope.async {
                        auctionViews = auctionViews + controller.getAsteSearch(indice++, token)
                        applicaFiltri()
                    }
                }
            }
        })



        return binding.root
    }





    private fun applicaFiltri(){
        filteredList.clear()
        if(filterCounter==0){
            filteredList.addAll(auctionViews)
        }else{
            if(statusFiltroFumetti){
                filteredList.addAll(auctionViews.filter { it.categoria == "Fumetti & Manga" })
            }
            if(statusFiltroActionFigures){
                filteredList.addAll(auctionViews.filter { it.categoria == "Action Figures" })
            }
            if(statusFiltroCarte){
                filteredList.addAll(auctionViews.filter { it.categoria == "Carte Collezionabili" })
            }
            if(statusFiltroTavole){
                filteredList.addAll(auctionViews.filter { it.categoria == "Tavole Originali" })
            }
            if(statusFiltroGadget){
                filteredList.addAll(auctionViews.filter { it.categoria == "Gadget e altro" })
            }
        }
        searchList.clear()
        if(filtroTestuale.isNotEmpty()) {
            filteredList.forEach {
                if (it.titoloAsta.toLowerCase(Locale.getDefault()).contains(filtroTestuale)) {
                    searchList.add(it)
                }
            }
        }else{
            searchList.addAll(filteredList)
        }
        if(searchList.isEmpty()){
            Toast.makeText(this.context, "Nessuna asta trovata!", Toast.LENGTH_SHORT).show()
        }
        binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
    }



}