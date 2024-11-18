package com.danilo.lootmarket

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.lootmarket.Network.dto.MyToken
import com.danilo.lootmarket.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    //private lateinit var auctionsAdapter : AuctionsAdapter

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mail: String = intent.getStringExtra("mail").toString()
        val token: String = intent.getStringExtra("token").toString()
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


            bottomNavigationView = findViewById(R.id.bottom_navigation)
            bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment(mail, token), "HomeFragment")
                    true
                }

                R.id.bottom_seach -> {
                    replaceFragment(SearchFragment(mail, token), "SearchFragment")
                    true
                }

                R.id.bottom_add -> {
                    val intent = Intent(this, AddAuctionActivity::class.java)
                    intent.putExtra("mail", mail)
                    intent.putExtra("token", token)
                    startActivity(intent)
                    true
                }

                R.id.bottom_auctions -> {
                    replaceFragment(AuctionsFragment(mail, token), "AuctionsFragment")
                    true
                }

                R.id.bottom_profile -> {
                    replaceFragment(ProfileFragment(mail, token), "ProfileFragment")
                    true
                }

                else -> false
            }
        }

        binding.NotificationButtonPaginaHome.setOnClickListener{
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }


        replaceFragment(HomeFragment(mail, token), "HomeFragment")



    }

    private fun replaceFragment(fragment: Fragment, tag: String){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment, tag).commit()

    }

    override fun onResume(){
        super.onResume()
        if(supportFragmentManager.fragments.last().tag == "HomeFragment"){
            bottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true)
        }else if(supportFragmentManager.fragments.last().tag == "SearchFragment"){
            bottomNavigationView.getMenu().findItem(R.id.bottom_seach).setChecked(true)
        }else if(supportFragmentManager.fragments.last().tag == "AuctionsFragment"){
            bottomNavigationView.getMenu().findItem(R.id.bottom_auctions).setChecked(true)
        }else if(supportFragmentManager.fragments.last().tag == "ProfileFragment"){
            bottomNavigationView.getMenu().findItem(R.id.bottom_profile).setChecked(true)
        }
    }

}





