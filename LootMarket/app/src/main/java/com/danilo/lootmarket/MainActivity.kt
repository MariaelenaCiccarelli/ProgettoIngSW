package com.danilo.lootmarket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.network.RetrofitInstance
import com.danilo.lootmarket.network.dto.MyToken
import com.danilo.lootmarket.network.dto.UtenteAutenticazioneDTO
import com.danilo.lootmarket.databinding.ActivityMainBinding
import kotlinx.coroutines.async
import okio.IOException
import retrofit2.HttpException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var contesto = this

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
            if((mailUtente != "") && (passwordUtente != "")){
                var utenteautenticazioneDTO = UtenteAutenticazioneDTO("", "", "", passwordUtente, mailUtente, 0, 0, 0,"", "" )
                postAccediUtente(utenteautenticazioneDTO)
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


    private fun postAccediUtente(utenteAutenticazioneDTO: UtenteAutenticazioneDTO){
        lifecycleScope.async {
            val response = try{
                RetrofitInstance.api.postAccediUtente(utenteAutenticazioneDTO)
            }catch (e: IOException){
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                Toast.makeText(contesto, "Autenticazione fallita!", Toast.LENGTH_SHORT).show()
                return@async
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                Toast.makeText(contesto, "Autenticazione fallita!", Toast.LENGTH_SHORT).show()
                return@async
            }catch (e: Exception){
                Log.e("MyNetwork", e.toString())
                Log.e("MyNetwork", e.cause.toString())
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.println(Log.INFO, "MyNetwork", "Response is successful")
                var jwtToken: MyToken = response.body()!!
                Toast.makeText(contesto, "Accesso avvenuto con successo!", Toast.LENGTH_SHORT).show()
                binding.editTextEmailPaginaAccedi.setText("")
                binding.editTextPasswordPaginaAccedi.setText("")
                val intent = Intent(contesto, HomeActivity::class.java)
                intent.putExtra("mail", utenteAutenticazioneDTO.mail)
                intent.putExtra("token", jwtToken.token)
                intent.putExtra("isBusiness", jwtToken.business)
                startActivity(intent)
                return@async
            }else{
                Log.e("MyNetwork", "Response not successful")
                Toast.makeText(contesto, "Autenticazione fallita!", Toast.LENGTH_SHORT).show()
                return@async
            }
        }
    }





}