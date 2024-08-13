package com.danilo.lootmarket

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.danilo.lootmarket.databinding.ActivityRecoveryBinding

class RecoveryActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRecoveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottoneRecuperaPaginaRecovery.setOnClickListener{
            Toast.makeText(this, "Email di recupero password inviata!", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.bottoneIndietroPaginaSignup.setOnClickListener{
            finish()
        }
    }

}