package com.danilo.lootmarket

import android.os.Bundle
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.databinding.ActivityHomeBinding

class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private lateinit var auctionsAdapter : AuctionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var auction1 = Auction(0, "Naruto", 150F, 3, "prova", "Action figure originale in vinile di Naruto Uzumaki")
        var auction2 = Auction(0, "Drago Bianco Occhi Blu Rara Ghost", 15000F, 20, "prova", "Carta originale pazza incredibile di yu-gi-oh")
        var auction3 = Auction(0, "Pennino Originale Giuro di Masashi Kishimoto", 150.00F, 0, "prova", "Me lo ha portato mio zio dal Giappone giuro su mio zio")
        var auction4 = Auction(0, "Pennino Originale Giuro di Masashi Kishimoto", 150.00F, 0, "prova", "Me lo ha portato mio zio dal Giappone giuro su mio zio")
        var auction5 = Auction(0, "Pennino Originale Giuro di Masashi Kishimoto", 150.00F, 0, "prova", "Me lo ha portato mio zio dal Giappone giuro su mio zio")
        var auction6 = Auction(0, "Pennino Originale Giuro di Masashi Kishimoto", 150.00F, 0, "prova", "Me lo ha portato mio zio dal Giappone giuro su mio zio")
        var auction7 = Auction(0, "Pennino Originale Giuro di Masashi Kishimoto", 150.00F, 0, "prova", "Me lo ha portato mio zio dal Giappone giuro su mio zio")

        var auctions: List<Auction>
        auctions = listOf(auction1, auction2, auction3, auction4, auction5, auction6, auction7)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auctionsAdapter = AuctionsAdapter(auctions, this)

        binding.RecyclerViewPaginaHome.layoutManager = LinearLayoutManager(this)
        binding.RecyclerViewPaginaHome.adapter = auctionsAdapter






    }
    /*
    override fun onResume(){
        super.onResume()
        auctionsAdapter.refreshData()
    }

     */

}