package com.danilo.lootmarket

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.danilo.lootmarket.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        enableEdgeToEdge()
        Thread.sleep(1000)
        installSplashScreen()


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottoneAccediPaginaAccedi.setOnClickListener{
            var mailUtente = binding.editTextEmailPaginaAccedi.text.toString()
            var passwordUtente = binding.editTextPasswordPaginaAccedi.text.toString()
            if((mailUtente == "lootmarket@gmail.com") && (passwordUtente == "lootmarket@24")){
                Toast.makeText(this, "Accesso eseguito", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Credenziali errate", Toast.LENGTH_SHORT).show()
            }
        }
        binding.bottoneRegistratiPaginaAccedi.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.bottoneRecoveryPaginaAccedi.setOnClickListener{
            val intent = Intent(this, RecoveryActivity::class.java)
            startActivity(intent)
        }

    }
}