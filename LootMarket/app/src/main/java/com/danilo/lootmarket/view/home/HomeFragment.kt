package com.danilo.lootmarket.view.home
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danilo.lootmarket.Controller.Controller
import com.danilo.lootmarket.R
import com.danilo.lootmarket.view.adapters.AuctionsAdapter
import com.danilo.lootmarket.databinding.FragmentHomeBinding
import com.danilo.lootmarket.view.home.details.AuctionDetailsFragment
import kotlinx.coroutines.async


class HomeFragment(var mail: String, var token: String, var detailsAsta: Boolean, var idAstaDetails: Int): Fragment(){

    private lateinit var binding: FragmentHomeBinding
    private var indice: Int =0
    private lateinit var auctionsAdapter: AuctionsAdapter
    private lateinit var controller: Controller

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {


        indice=0

        if(detailsAsta==true){
            detailsAsta=false
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(idAstaDetails, mail, token))
            transaction?.addToBackStack(this.tag)
            transaction?.commit()
        }


        controller = Controller()
        binding = FragmentHomeBinding.inflate(layoutInflater)
        auctionsAdapter = AuctionsAdapter(arrayListOf())
        binding.RecyclerViewFrammentoHome.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoHome.adapter = auctionsAdapter

        lifecycleScope.async {
            if(!controller.getAste(indice, token,
                    binding.RecyclerViewFrammentoHome.adapter as AuctionsAdapter
                )){
                activity?.finish()
            }
        }
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
                    lifecycleScope.async {
                        controller.getAste(indice, token,
                            binding.RecyclerViewFrammentoHome.adapter as AuctionsAdapter
                        )
                    }
                    indice++
                }
            }
        })

        return binding.root
    }


}





