package com.danilo.lootmarket

import android.content.Intent
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.danilo.lootmarket.databinding.ActivityProfiledataBinding
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

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
        binding.editTextCellularePaginaProfileData.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }
            override fun onDestroyActionMode(mode: ActionMode) {}
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }
        }
        binding.bottoneConfermaPaginaProfileData.setOnClickListener{
            var nome = binding.editTextNomePaginaProfileData.text.toString()
            var cognome = binding.editTextCognomePaginaProfileData.text.toString()
            var codiceFiscale = binding.editTextCodiceFiscalePaginaProfileData.text.toString()
            var cellulare = binding.editTextCellularePaginaProfileData.text.toString()
            var dataNascita = LocalDate.of(binding.datePickerPaginaProfileData.year, binding.datePickerPaginaProfileData.month, binding.datePickerPaginaProfileData.dayOfMonth)
            val patternCodiceFiscale = Pattern.compile("^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]\$")
            
            if((nome == "")|| (cognome =="")||(codiceFiscale == "")||(cellulare=="")){
                Toast.makeText(this, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()

            }else if(!(patternCodiceFiscale.matcher(codiceFiscale).matches())){
                Toast.makeText(this, "Codice Fiscale non valido", Toast.LENGTH_SHORT).show()
            }else if((dataNascita.until(LocalDate.now(), ChronoUnit.YEARS))<18){
                Toast.makeText(this, "Devi essere maggiorenne per iscriverti!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Registrazione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        setContentView(binding.root)


    }
}