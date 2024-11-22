package com.danilo.lootmarket

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColor
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danilo.lootmarket.Network.RetrofitInstance
import com.danilo.lootmarket.Network.dto.AstaDTO
import com.danilo.lootmarket.Network.dto.MyToken
import com.danilo.lootmarket.databinding.FragmentSearchBinding
import kotlinx.coroutines.async
import okio.IOException
import retrofit2.HttpException
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Base64
import java.util.Locale


class SearchFragment(var mail: String, var token: String) : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var auctionsAdapter: AuctionsAdapter
    private lateinit var searchView: SearchView
    private lateinit var filteredList: ArrayList<Auction>
    private lateinit var searchList: ArrayList<Auction>
    private lateinit var auctions: List<Auction>
    private var indice: Int = 0
    private var statusFiltroFumetti: Boolean = false
    private var statusFiltroActionFigures: Boolean = false
    private var statusFiltroCarte: Boolean = false
    private var statusFiltroTavole: Boolean = false
    private var statusFiltroGadget: Boolean = false
    private var filterCounter: Int = 0;


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment
        indice=0
        auctions = listOf()
        getAste(indice++)


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






        //FILTRO FUMETTI
        binding.cardFiltroFumettiFrammentoSearch.setOnClickListener {
            if (!statusFiltroFumetti) {
                binding.immagineFiltroFumettiFrammentoSearch.setImageResource(R.drawable.baseline_auto_stories_24_secondcolor)
                binding.cardFiltroFumettiFrammentoSearch.setCardBackgroundColor(Color.parseColor("#a91010"))
                statusFiltroFumetti = true
                filterCounter++
                ApplicaFiltri()
            } else {
                statusFiltroFumetti = false
                filterCounter--
                ApplicaFiltri()
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
                ApplicaFiltri()
            }else{
                statusFiltroActionFigures = false
                filterCounter--
                ApplicaFiltri()
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
                ApplicaFiltri()
            } else {
                statusFiltroCarte = false
                filterCounter--
                ApplicaFiltri()
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
                ApplicaFiltri()
            } else {
                statusFiltroTavole = false
                filterCounter--
                ApplicaFiltri()
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
                ApplicaFiltri()
            } else {
                statusFiltroGadget = false
                filterCounter--
                ApplicaFiltri()
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
                    getAste(indice)
                    indice++
                }
            }
        })


        //val view = inflater.inflate(R.layout.fragment_home, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAste(indice: Int){
        var auctionsCaricate = ArrayList<Auction>()
        lifecycleScope.async {

            val response = try{
                RetrofitInstance.api.getAsteHome(indice, token)
            }catch (e: IOException){
                Log.e("MyNetwork", "IOException, you might not have internet connection")
                return@async
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                var asteRecuperate: List<AstaDTO> = response.body()!!

                for(asta in asteRecuperate){
                    Log.println(Log.INFO, "MyNetwork", asta.toString())
                    val immagineAstaByteArrayDecoded = Base64.getDecoder().decode(asta.immagineAsta)
                    val immagineAsta= BitmapFactory.decodeByteArray(immagineAstaByteArrayDecoded, 0, immagineAstaByteArrayDecoded.size)
                    var auction = Auction(asta.idAsta, asta.titolo, asta.ultimaOfferta, ZonedDateTime.of(asta.anno, asta.mese, asta.giorno, 0, 0, 0,0, ZoneId.of("GMT")), immagineAsta, asta.descrizione, asta.categoria, asta.tipoAsta )
                    auctionsCaricate.add(auction)

                }
                auctions = auctions + auctionsCaricate
                ApplicaFiltri()
                return@async
            }else{
                Log.e("MyNetwork", "Response not successful")
                if(response.code().toString().equals("401")){
                    Log.e("MyNetwork", response.code().toString()+" Token Scaduto!")
                }
                activity?.finish()
                return@async
            }
        }
    }

    private fun ApplicaFiltri(){
        filteredList.clear()
        if(filterCounter==0){
            filteredList.addAll(auctions)
        }else{
            if(statusFiltroFumetti){
                filteredList.addAll(auctions.filter { it.Categoria == "Fumetti" })
            }
            if(statusFiltroActionFigures == true){
                filteredList.addAll(auctions.filter { it.Categoria == "Action Figures" })
            }
            if(statusFiltroCarte){
                filteredList.addAll(auctions.filter { it.Categoria == "Carte Collezionabili" })
            }
            if(statusFiltroTavole){
                filteredList.addAll(auctions.filter { it.Categoria == "Tavole" })
            }
            if(statusFiltroGadget){
                filteredList.addAll(auctions.filter { it.Categoria == "Gadget" })
            }
        }
        searchList.clear()
        searchList.addAll(filteredList)
        binding.RecyclerViewFrammentoSearch.adapter!!.notifyDataSetChanged()
    }
}