package com.danilo.lootmarket

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.danilo.lootmarket.databinding.ActivityProfiledataBinding

class DataProfileActivity: AppCompatActivity() {

    private lateinit var binding: ActivityProfiledataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfiledataBinding.inflate(layoutInflater)

        val spinner : Spinner = binding.spinnerNazionePaginaProfileData
        ArrayAdapter.createFromResource(
            this,
            R.array.countries_array,
            android.R.layout.simple_spinner_item
        ).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        binding.bottoneConfermaPaginaProfileData.setOnClickListener{
            var nome = binding.editTextNomePaginaProfileData.text.toString()
            var cognome = binding.editTextCognomePaginaProfileData.text.toString()
            var codiceFiscale = binding.editTextCodiceFiscalePaginaProfileData.text.toString()
            var cellulare = binding.editTextCellularePaginaProfileData.text.toString()
            
            if((nome == "")|| (cognome =="")||(codiceFiscale == "")||(cellulare=="")){
                Toast.makeText(this, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Registrazione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        setContentView(binding.root)


    }
}