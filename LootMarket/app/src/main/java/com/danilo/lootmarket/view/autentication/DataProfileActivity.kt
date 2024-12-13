package com.danilo.lootmarket.view.autentication

import android.content.Context
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
import androidx.lifecycle.lifecycleScope
import com.danilo.lootmarket.Controller.Controller
import com.danilo.lootmarket.R

import com.danilo.lootmarket.databinding.ActivityProfiledataBinding
import com.danilo.lootmarket.view.home.HomeActivity
import kotlinx.coroutines.async

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

class DataProfileActivity: AppCompatActivity() {

    private var contesto: Context = this
    private lateinit var controller: Controller
    private lateinit var binding: ActivityProfiledataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email: String = intent.getStringExtra("email").toString()
        val password: String = intent.getStringExtra("password").toString()
        enableEdgeToEdge()
        binding = ActivityProfiledataBinding.inflate(layoutInflater)
        controller = Controller()


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
            val nazione = binding.spinnerNazionePaginaProfileData.selectedItem.toString()
            
            if((nome == "")|| (cognome =="")||(codiceFiscale == "")||(cellulare=="")){
                Toast.makeText(this, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()

            }else if(!(patternCodiceFiscale.matcher(codiceFiscale).matches())){
                Toast.makeText(this, "Codice Fiscale non valido", Toast.LENGTH_SHORT).show()
            }else if((dataNascita.until(LocalDate.now(), ChronoUnit.YEARS))<18){
                Toast.makeText(this, "Devi essere maggiorenne per iscriverti!", Toast.LENGTH_SHORT).show()
            }else{



                lifecycleScope.async {
                    var (status, jwtToken) = controller.postRegistraUtente(nome, cognome, codiceFiscale, password,  email, dataNascita.year, dataNascita.monthValue, dataNascita.dayOfMonth, nazione, cellulare)
                    if(status==-1 || status == -2 || jwtToken == ""){
                        if(status==-1){
                            Toast.makeText(contesto, "Registrazione fallita!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(contesto, MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(contesto, "Email già in utilizzo!", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(contesto, "Registrazione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(contesto, HomeActivity::class.java)
                        intent.putExtra("mail", email)
                        intent.putExtra("token", jwtToken)
                        startActivity(intent)

                    }
                }
            }
        }
        setContentView(binding.root)
    }







}