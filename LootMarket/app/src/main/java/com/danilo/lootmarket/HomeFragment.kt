package com.danilo.lootmarket
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danilo.lootmarket.network.RetrofitInstance
import com.danilo.lootmarket.network.dto.AstaDTO
import com.danilo.lootmarket.databinding.FragmentHomeBinding
import kotlinx.coroutines.async
import okio.IOException
import retrofit2.HttpException
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Base64


class HomeFragment(var mail: String, var token: String, var detailsAsta: Boolean, var idAstaDetails: Int): Fragment(){

    private lateinit var binding: FragmentHomeBinding
    private var indice: Int =0
    private lateinit var auctionsAdapter: AuctionsAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {


        indice=0
        var auctions: ArrayList<Auction>
        auctions = arrayListOf()


        if(detailsAsta==true){
            detailsAsta=false
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(idAstaDetails, mail, token))
            transaction?.addToBackStack(this.tag)
            transaction?.commit()
        }



        binding = FragmentHomeBinding.inflate(layoutInflater)
        auctionsAdapter = AuctionsAdapter(auctions)
        binding.RecyclerViewFrammentoHome.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoHome.adapter = auctionsAdapter
        getAste(indice)
        indice++


        auctionsAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id, mail, token))
            transaction?.addToBackStack(this.tag)
            transaction?.commit()
        }


        binding.RecyclerViewFrammentoHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getAste(indice)
                    indice++
                }
            }
        })

        return binding.root


    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun getAste(indice: Int){
        var auctions2 = ArrayList<Auction>()
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
                Log.e("MyNetwork", "Response is successful")
                var asteRecuperate: List<AstaDTO> = response.body()!!
                for(asta in asteRecuperate){
                    val immagineAstaByteArrayDecoded = Base64.getDecoder().decode(asta.immagineAsta)
                    val immagineAsta= BitmapFactory.decodeByteArray(immagineAstaByteArrayDecoded, 0, immagineAstaByteArrayDecoded.size)
                    var auction = Auction(asta.idAsta, asta.titolo, asta.ultimaOfferta, ZonedDateTime.of(asta.anno, asta.mese, asta.giorno, 0, 0, 0,0, ZoneId.of("GMT")), immagineAsta, asta.descrizione, asta.categoria, asta.tipoAsta )
                    auctions2.add(auction)
                }
                (binding.RecyclerViewFrammentoHome.adapter as AuctionsAdapter).auctions.addAll(auctions2)
                binding.RecyclerViewFrammentoHome.adapter!!.notifyItemRangeInserted(binding.RecyclerViewFrammentoHome.adapter!!.itemCount+1, auctions2.size);
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





}





