package com.danilo.lootmarket

import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity: AppCompatActivity() {
   // private lateinit var binding: ActivityHomeBinding

    //private lateinit var auctionsAdapter : AuctionsAdapter

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        /*
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
        binding.RecyclerViewPaginaHome.adapter = auctionsAdapter*/



            bottomNavigationView = findViewById(R.id.bottom_navigation)
             bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.bottom_seach -> {
                    replaceFragment(SearchFragment())
                    true
                }

                R.id.bottom_add -> {
                    Toast.makeText(this, "Upload Image & Video", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.bottom_reels -> {
                    replaceFragment(AuctionsFragment())
                    true
                }

                R.id.bottom_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }
        replaceFragment(HomeFragment())


    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()

    }
}
    /*
    override fun onResume(){
        super.onResume()
        auctionsAdapter.refreshData()
    }

     */

