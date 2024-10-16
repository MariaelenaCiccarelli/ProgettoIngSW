package com.danilo.lootmarket
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Network.MyApi
import com.danilo.lootmarket.Network.RetrofitInstance
import com.danilo.lootmarket.Network.dto.AstaDTO
import com.danilo.lootmarket.databinding.FragmentHomeBinding
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.ZoneId
import java.time.ZonedDateTime


class HomeFragment: Fragment(){

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

        var auctions: ArrayList<Auction>
        auctions = arrayListOf(auction1, auction2, auction3, auction4, auction5, auction6, auction7)

        //auctions = listOf(getAste(0).get(0))








        binding = FragmentHomeBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        auctionsAdapter = AuctionsAdapter(auctions, this.requireContext())

        binding.RecyclerViewFrammentoHome.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoHome.adapter = auctionsAdapter

        var auctionsArray = ArrayList<Auction>()
        auctionsArray = getAste(1)

        auctionsAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id))
            transaction?.addToBackStack(this.toString())
            transaction?.commit()
        }

        //val view = inflater.inflate(R.layout.fragment_home, container, false)
        return binding.root
    }

    private fun getAste(indice: Int): ArrayList<Auction>{
        var auctions = ArrayList<Auction>()
        lifecycleScope.async {

            val response = try{
                RetrofitInstance.api.getAsteHome(indice)
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
                    var auction = Auction(asta.idAsta, asta.titolo, asta.ultimaOfferta, ZonedDateTime.of(asta.anno, asta.mese, asta.giorno, 0, 0, 0,0, ZoneId.of("GMT")), (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable), asta.descrizione, asta.categoria, asta.tipoAsta )
                    auctions.add(auction)
                    auctionsAdapter.auctions = auctionsAdapter.auctions + auction
                    binding.RecyclerViewFrammentoHome.adapter = auctionsAdapter
                    Log.println(Log.INFO, "MyNetwork", auctions[0].titoloAsta)
                }
                return@async
            }else{
                Log.e("MyNetwork", "Response not successful")
                return@async
            }
        }
        return auctions
    }

    /*
    private fun getAste(indice: Int): ArrayList<Auction>{
        var auctions = arrayListOf<Auction>()
        val api = Retrofit.Builder()
            .baseUrl("http://192.168.56.1:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
        api.getAsteHome(indice).enqueue(object : Callback<List<AstaHomeRecuperate>>{
            override fun onResponse(call: Call<List<AstaHomeRecuperate>>, response: Response<List<AstaHomeRecuperate>>) {
                Log.println(Log.INFO, "MyNetwork", response.code().toString())
                if(response.isSuccessful){
                    Log.println(Log.INFO, "MyNetwork", response.code().toString())
                    response.body()?.let{
                        Log.println(Log.INFO, "MyNetwork", response.code().toString())
                        for(asta in it){
                            Log.println(Log.INFO, "MyNetwork", asta.toString())
                            var auction = Auction(asta.idAsta, asta.titolo, asta.ultimaOfferta, ZonedDateTime.of(asta.anno, asta.mese, asta.giorno, 0, 0, 0,0, ZoneId.of("GMT")), (ResourcesCompat.getDrawable(resources, R.drawable.naruto2, null) as Drawable), asta.descrizione, asta.categoria, asta.tipoAsta )
                            auctions.add(auction)
                            Log.println(Log.INFO, "MyNetwork", auctions[0].titoloAsta)
                        }
                    }

                }
            }

            override fun onFailure(call: Call<List<AstaHomeRecuperate>>, t: Throwable) {
                Log.println(Log.INFO, "MyNetwork", t.cause.toString())
                Log.println(Log.INFO, "MyNetwork", t.message.toString())
            }

        })
        Log.println(Log.INFO, "MyNetwork", auctions[0].titoloAsta)
        return auctions
    }
     */
}





