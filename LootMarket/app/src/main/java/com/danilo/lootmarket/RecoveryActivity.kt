package com.danilo.lootmarket

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.danilo.lootmarket.databinding.ActivityRecoveryBinding
import java.util.regex.Pattern

class RecoveryActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRecoveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val patternMail = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")

        binding.bottoneRecuperaPaginaRecovery.setOnClickListener{
            var email = binding.editTextEmailPaginaRecovery.text.toString()
            if( email== ""){
                Toast.makeText(this, "Compila il campo email!", Toast.LENGTH_SHORT).show()
            }else if(!(patternMail.matcher(email).matches())){
                Toast.makeText(this, "Formato email non valido", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Email di recupero password inviata!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        binding.bottoneIndietroPaginaSignup.setOnClickListener{
            finish()
        }
    }

}