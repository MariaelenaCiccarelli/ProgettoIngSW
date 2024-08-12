package com.danilo.lootmarket

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.danilo.lootmarket.databinding.ActivitySignupBinding

class SignupActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

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

            if((email == "")||(password=="")||(checked==false)){
                Toast.makeText(this, "Compila tutti i campi e accetta i termini!", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, DataProfileActivity::class.java)
                startActivity(intent)
            }



        }
    }
}