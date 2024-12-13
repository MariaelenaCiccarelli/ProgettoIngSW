package com.danilo.lootmarket.view.autentication

import android.content.Intent
import android.os.Bundle

import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Controller.Controller

import com.danilo.lootmarket.databinding.ActivityMainBinding
import com.danilo.lootmarket.view.home.HomeActivity
import kotlinx.coroutines.async



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var contesto = this
    private lateinit var controller: Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        enableEdgeToEdge()
        Thread.sleep(1000)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        controller = Controller()

        binding.bottoneAccediPaginaAccedi.setOnClickListener{
            var mailUtente = binding.editTextEmailPaginaAccedi.text.toString()
            var passwordUtente = binding.editTextPasswordPaginaAccedi.text.toString()
            if((mailUtente != "") && (passwordUtente != "")){

                lifecycleScope.async {
                    var (status,jwtToken) = controller.postAccediUtente(passwordUtente, mailUtente)
                    if(status && jwtToken!=null){
                        Toast.makeText(contesto, "Accesso avvenuto con successo!", Toast.LENGTH_SHORT).show()
                        binding.editTextEmailPaginaAccedi.setText("")
                        binding.editTextPasswordPaginaAccedi.setText("")
                        val intent = Intent(contesto, HomeActivity::class.java)
                        intent.putExtra("mail", mailUtente)
                        intent.putExtra("token", jwtToken.token)
                        intent.putExtra("isBusiness", jwtToken.business)
                        startActivity(intent)
                    }else{
                        Toast.makeText(contesto, "Autenticazione fallita!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(this, "Credenziali invalide", Toast.LENGTH_SHORT).show()
            }
        }


        binding.bottoneRegistratiPaginaAccedi.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }


}