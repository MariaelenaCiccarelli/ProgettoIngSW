package com.danilo.lootmarket


import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.databinding.FragmentPersonalAuctionsBinding
import com.danilo.lootmarket.databinding.FragmentSubscribedAuctionsBinding
import java.time.ZoneId
import java.time.ZonedDateTime

class PersonalAuctionsFragment(private var auctions: List<AuctionViewHistory>, var mail: String, private var token: String) : Fragment() {

    private lateinit var binding: FragmentPersonalAuctionsBinding

    private lateinit var auctionsLiveAdapter: AuctionsLiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentPersonalAuctionsBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        auctions = auctions.filter { (it.autore.equals(mail)) }
        auctionsLiveAdapter = AuctionsLiveAdapter(auctions, this.requireContext(), false)

        binding.RecyclerViewFrammentoPersonalAuctions.layoutManager = LinearLayoutManager(this.requireContext())
        binding.RecyclerViewFrammentoPersonalAuctions.adapter = auctionsLiveAdapter

        auctionsLiveAdapter.onItemClick = {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, AuctionDetailsFragment(it.id,mail, token))
            transaction?.addToBackStack(this.tag)
            transaction?.commit()
        }



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_subscribed_auctions, container, false)
        return binding.root
    }
}