package com.danilo.lootmarket.view.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.danilo.lootmarket.R
import com.danilo.lootmarket.databinding.ActivityHomeBinding
import com.danilo.lootmarket.view.home.details.ProfileFragment
import com.danilo.lootmarket.view.home.personal_auctions.AuctionsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mail: String
    private lateinit var token: String
    private var isBusiness: Boolean = false
    private var detailsAsta: Boolean = false
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mail= intent.getStringExtra("mail").toString()
        token= intent.getStringExtra("token").toString()
        isBusiness= intent.getBooleanExtra("isBusiness", false)
        detailsAsta = intent.getBooleanExtra("detailsAsta", false)
        var idAstaDetails = intent.getIntExtra("idAstaDetails", 0)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment(mail, token, detailsAsta, idAstaDetails ), "HomeFragment")
                    detailsAsta=false
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
                    intent.putExtra("isBusiness", isBusiness)
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
            intent.putExtra("mail", mail)
            intent.putExtra("token", token)
            startActivity(intent)
        }


        replaceFragment(HomeFragment(mail, token, detailsAsta, idAstaDetails), "HomeFragment")
        detailsAsta=false


    }

    private fun replaceFragment(fragment: Fragment, tag: String){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment, tag).commit()

    }

    override fun onResume(){
        super.onResume()
        if(supportFragmentManager.fragments.last().tag == "HomeFragment"){
            bottomNavigationView.menu.findItem(R.id.bottom_home).setChecked(true)
        }else if(supportFragmentManager.fragments.last().tag == "SearchFragment"){
            bottomNavigationView.menu.findItem(R.id.bottom_seach).setChecked(true)
        }else if(supportFragmentManager.fragments.last().tag == "AuctionsFragment"){
            bottomNavigationView.menu.findItem(R.id.bottom_auctions).setChecked(true)
        }else if(supportFragmentManager.fragments.last().tag == "ProfileFragment"){
            bottomNavigationView.menu.findItem(R.id.bottom_profile).setChecked(true)
        }
    }

}





