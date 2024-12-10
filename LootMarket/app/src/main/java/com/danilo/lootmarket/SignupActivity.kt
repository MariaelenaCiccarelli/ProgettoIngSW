package com.danilo.lootmarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.network.RetrofitInstance
import com.danilo.lootmarket.databinding.ActivitySignupBinding
import kotlinx.coroutines.async
import okio.IOException
import retrofit2.HttpException
import java.util.regex.Pattern

class SignupActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var contesto: Context = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottoneAccediPaginaSignup.setOnClickListener{
            finish()
        }


        binding.bottoneRegistratiPaginaSignup.setOnClickListener{
            var email = binding.editTextEmailPaginaSignup.text.toString()
            var password = binding.editTextPasswordPaginaSignup.text.toString()
            var checked = binding.checkboxTerminiPaginaSignup.isChecked
            val patternMail = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
            if((email == "")||(password=="")||(checked==false)){
                Toast.makeText(this, "Compila tutti i campi e accetta i termini!", Toast.LENGTH_SHORT).show()
            }else if(!(patternMail.matcher(email).matches())){
                Toast.makeText(this, "Formato email non valido", Toast.LENGTH_SHORT).show()
            }else{
                getEsisteUtente(email, password)
            }
        }
    }

    private fun getEsisteUtente(email: String, password: String){
        lifecycleScope.async {
            val response = try{
                RetrofitInstance.api.getEsisteUtente(email)
            }catch (e: IOException){
                Log.e("MyNetwork", e.message.toString())
                Log.e("MyNetwork", e.cause.toString())
                startActivity(intent)
                return@async
            }catch (e: HttpException){
                Log.e("MyNetwork", "HttpException, unexpected response")
                Toast.makeText(contesto, "Registrazione fallita!", Toast.LENGTH_SHORT).show()
                val intent = Intent(contesto, MainActivity::class.java)
                startActivity(intent)
                return@async
            }
            if(response.isSuccessful && response.body() != null){
                Log.e("MyNetwork", "Response is successful")
                var status = response.body()
                if(status==1){
                    Toast.makeText(contesto, "Email già in utilizzo!", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(contesto, DataProfileActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("password", password)
                    startActivity(intent)
                }
                return@async
            }else{
                Toast.makeText(contesto, "Registrazione fallita!", Toast.LENGTH_SHORT).show()
                val intent = Intent(contesto, MainActivity::class.java)
                startActivity(intent)
                return@async
            }
        }
    }


}