package com.danilo.lootmarket

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.danilo.lootmarket.databinding.ActivityAddAuctionBinding
import com.danilo.lootmarket.databinding.ActivityRecoveryBinding

class AddAuctionActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAddAuctionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddAuctionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imageViewBackButtonPaginaAddAuction.setOnClickListener{
            finish()
        }
    }
}