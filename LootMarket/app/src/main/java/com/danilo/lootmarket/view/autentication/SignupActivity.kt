package com.danilo.lootmarket.view.autentication

import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Controller.Controller
import com.danilo.lootmarket.databinding.ActivitySignupBinding
import kotlinx.coroutines.async

import java.util.regex.Pattern

class SignupActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var contesto: Context = this
    private lateinit var controller: Controller


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        controller = Controller()


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
                lifecycleScope.async {
                    var status = controller.getEsisteUtente(email, password)
                    if(status==1){
                        val intent = Intent(contesto, DataProfileActivity::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("password", password)
                        startActivity(intent)
                    }else{
                        if(status==-2){
                            Toast.makeText(contesto, "Email già in utilizzo!", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(contesto, "Registrazione fallita!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(contesto, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }






}